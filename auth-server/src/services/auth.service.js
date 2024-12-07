const crypto = require('crypto');
const jwt = require('jsonwebtoken');
const UserModel = require('../models/user.model');
const TokenModel = require('../models/token.model');
const errorMessage = require('../errormessage/error.message');
class AuthService {
    // 비밀번호 해시 생성 (SHA-256)
    async hashPassword(password, salt) {
        return crypto
            .createHash('sha256')
            .update(password + salt)
            .digest('hex');
    }

    // 로그인
    async login(userId, password, rememberMe) {
        const user = await UserModel.findById(userId);
        if (!user) {
            throw new Error(errorMessage.INVALID_USER);
        }
        //'0: 활동 중, 1: 휴면, 2: 탈퇴(강퇴), 3: 미인증, 4: 잠금'
        // 계정 상태 확인
        if (user.status === 1) throw new Error(errorMessage.ACCOUNT_DORMANT);
        if (user.status === 2) throw new Error(errorMessage.ACCOUNT_RESTRICTED);
        if (user.status === 3) throw new Error(errorMessage.EMAIL_VERIFICATION_REQUIRED);
        if (user.status === 4) throw new Error(errorMessage.ACCOUNT_LOCKED);

        // 비밀번호 검증
        const hashedPassword = await this.hashPassword(password, user.salt);
        if (hashedPassword !== user.password) {
            await UserModel.incrementLoginTry(userId);
            if (user.loginTry >= 4) {
                await UserModel.updateStatus(userId, 4);
                throw new Error(errorMessage.ACCOUNT_LOCK);
            }
            throw new Error(errorMessage.INVALID_PASSWORD);
        }

        // 로그인 성공 처리
        await UserModel.resetLoginTry(userId);
        
        // JWT 토큰 생성 (SHA-256)
        const jti = crypto.randomBytes(16).toString('hex');
        const token = jwt.sign(
            { 
                userId: user.userId,
                email: user.email,
                name: user.name,
                jti: jti
            },
            process.env.JWT_SECRET,
            { 
                expiresIn: rememberMe ? '7d' : '1h',
                algorithm: 'HS256'  // SHA-256 명시
            }
        );

        // 토큰 저장
        const expiresAt = new Date();
        // 7일 또는 1일 후 만료 (rememberMe가 true일 경우 7일, false일 경우 1일)
        expiresAt.setHours(expiresAt.getHours() + (rememberMe ? 24*7 : 24));
        // 기존 토큰 삭제
        await TokenModel.removeUserTokens(userId);
        // 새로운 토큰 저장
        await TokenModel.saveToken(userId, jti, expiresAt);

        return {
            token,
            user: {
                userId: user.userId,
                name: user.name,
                email: user.email,
                phone: user.phone,
                status: user.status
            }
        };
    }

    // 로그아웃
    async logout(userId) {
        await TokenModel.removeUserTokens(userId);
        return true;
    }
}

module.exports = new AuthService();
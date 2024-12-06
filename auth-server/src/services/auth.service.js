const crypto = require('crypto');
const jwt = require('jsonwebtoken');
const UserModel = require('../models/user.model');
const TokenModel = require('../models/token.model');

class AuthService {
    // 비밀번호 해시 생성 (SHA-256)
    async hashPassword(password, salt) {
        return crypto
            .createHash('sha256')
            .update(password + salt)
            .digest('hex');
    }

    // 로그인
    async login(userId, password) {
        const user = await UserModel.findById(userId);
        if (!user) {
            throw new Error('존재하지 않는 아이디입니다.');
        }

        // 계정 상태 확인
        if (user.status === 2) throw new Error('탈퇴한 계정입니다.');
        if (user.status === 3) throw new Error('이메일 인증이 필요합니다.');
        if (user.status === 4) throw new Error('잠긴 계정입니다. 관리자에게 문의하세요.');

        // 비밀번호 검증
        const hashedPassword = await this.hashPassword(password, user.salt);
        if (hashedPassword !== user.password) {
            await UserModel.incrementLoginTry(userId);
            if (user.loginTry >= 4) {
                await UserModel.updateStatus(userId, 4);
                throw new Error('로그인 시도 횟수 초과로 계정이 잠겼습니다.');
            }
            throw new Error('비밀번호가 일치하지 않습니다.');
        }

        // 로그인 성공 처리
        await UserModel.resetLoginTry(userId);
        
        // JWT 토큰 생성 (SHA-256)
        const jti = crypto.randomBytes(16).toString('hex');
        const token = jwt.sign(
            { 
                id: user.userId,
                email: user.email,
                name: user.name,
                jti: jti
            },
            process.env.JWT_SECRET,
            { 
                expiresIn: '24h',
                algorithm: 'HS256'  // SHA-256 명시
            }
        );

        // 토큰 저장
        const expiresAt = new Date();
        expiresAt.setHours(expiresAt.getHours() + 24);
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
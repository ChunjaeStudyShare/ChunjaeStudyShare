const UserModel = require('../models/user.model');
const TokenUtil = require('../util/token.util');
const errorMessage = require('../errormessage/error.message');
const crypto = require('crypto');

class AuthService {
    async login(userId, password, rememberMe) {
        // 1. 사용자 찾기
        const user = await UserModel.findById(userId);
        if (!user) {
            throw new Error(errorMessage.INVALID_CREDENTIALS);
        }

        // 2. 계정 상태 확인
        switch (user.status) {
            case 1:
                // 계정 휴면 상태 확인
                throw new Error(errorMessage.ACCOUNT_DORMANT);
            case 2:
                // 계정 제한 상태 확인 (정지)
                throw new Error(errorMessage.ACCOUNT_RESTRICTED);
            case 3:
                // 계정 인증 대기 상태 확인 (이메일 인증 대기)
                throw new Error(errorMessage.EMAIL_NOT_VERIFIED);
            case 4:
                // 계정 잠금 상태 확인 (로그인 시도 횟수 5회 초과)
                throw new Error(errorMessage.ACCOUNT_LOCKED);
        }

        // 3. 비밀번호 검증
        const hashedPassword = crypto
            .createHash('sha256')
            .update(password + user.salt)
            .digest('hex');

        if (hashedPassword !== user.password) {
            await UserModel.updateLoginTry(user.userId);

            // 로그인 시도 횟수 체크 및 계정 잠금 처리
            if (user.loginTry >= 4) {
                await UserModel.updateStatus(user.userId, 4);
                throw new Error(errorMessage.ACCOUNT_LOCK);
            }
            throw new Error(errorMessage.INVALID_CREDENTIALS);
        }

        // 4. 로그인 성공 처리
        await UserModel.resetLoginTry(user.userId);
        await UserModel.updateLastLogin(user.userId);

        // 5. 토큰 생성
        const token = TokenUtil.generateToken(user, rememberMe);

        return {
            success: true,
            token,
            user: {
                id: user.userId,
                name: user.name,
                email: user.email
            }
        };
    }
}

module.exports = new AuthService();
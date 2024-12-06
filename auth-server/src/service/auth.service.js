const crypto = require('crypto'); // Node.js 내장 모듈
const UserModel = require('../models/user.model');
const TokenBuilderUtil = require('../util/token.builder.util');

class AuthService {
    async login(id, password, rememberMe) {
        const user = await UserModel.findById(id);
        if (!user) {
            throw new Error('아이디 또는 비밀번호가 잘못되었습니다.');
        }

        // 휴면 계정 체크
        const sixMonthsAgo = new Date();
        sixMonthsAgo.setMonth(sixMonthsAgo.getMonth() - 6);
        
        if (user.lastLogin && new Date(user.lastLogin) < sixMonthsAgo) {
            await UserModel.updateStatus(user.id, 1);
            throw new Error('휴면 계정입니다. 계정을 활성화해주세요.');
        }

        // SHA256 해시 생성
        const hashedPassword = crypto
            .createHash('sha256')
            .update(password + user.salt) // salt 추가
            .digest('hex');

        // 비밀번호 검증
        if (hashedPassword !== user.password) {
            // 로그인 시도 횟수 증가
            await UserModel.updateLoginTry(user.id);
            throw new Error(`아이디 또는 비밀번호가 잘못되었습니다. 로그인 시도 횟수: ${user.loginTry}`);
        }
        await UserModel.resetLoginTry(user.id);
        // 마지막 로그인 시간 업데이트
        await UserModel.updateLastLogin(user.id);
        // jwt 토큰 발급
        return TokenBuilderUtil.generateToken(user);
    }
}

module.exports = new AuthService();
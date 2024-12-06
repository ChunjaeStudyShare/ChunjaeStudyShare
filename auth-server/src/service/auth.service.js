const crypto = require('crypto'); // Node.js 내장 모듈
const jwt = require('jsonwebtoken');
const UserModel = require('../models/user.model');

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
            await UserModel.updateLoginTry(user.id);
            throw new Error(`아이디 또는 비밀번호가 잘못되었습니다. 로그인 시도 횟수: ${user.loginTry}`);
        }

        await UserModel.updateLastLogin(user.id);

        const token = jwt.sign(
            {
                userId: user.id,
                email: user.email,
                name: user.name,
                status: user.status
            },
            process.env.JWT_SECRET,
            { expiresIn: rememberMe ? '7d' : '1h' }
        );

        return {
            token,
            user: {
                id: user.id,
                email: user.email,
                name: user.name,
                status: user.status
            }
        };
    }
}

module.exports = new AuthService();
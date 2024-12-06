const crypto = require('crypto');
const UserModel = require('../models/user.model');
const EmailCodeModel = require('../models/emailcode.model');

class UserService {
    // 비밀번호 해시 생성 (SHA-256)
    async hashPassword(password, salt) {
        return crypto
            .createHash('sha256')
            .update(password + salt)
            .digest('hex');
    }

    // 회원가입
    async register(userData) {
        // 아이디 중복 검사
        const existingUser = await UserModel.findById(userData.userId);
        if (existingUser) {
            throw new Error('이미 사용 중인 아이디입니다.');
        }

        // 이메일 중복 검사
        const existingEmail = await UserModel.findByEmail(userData.email);
        if (existingEmail) {
            throw new Error('이미 사용 중인 이메일입니다.');
        }

        // 솔트 생성 및 비밀번호 해시화
        const salt = crypto.randomBytes(32).toString('hex');
        const hashedPassword = await this.hashPassword(userData.password, salt);

        // 사용자 생성
        const user = {
            ...userData,
            password: hashedPassword,
            salt,
            status: 3  // 미인증 상태
        };

        await UserModel.create(user);
        
        // 인증 메일 발송 로직은 이메일 서비스에서 처리
        return user.userId;
    }

    // 회원 정보 수정
    async updateUser(userId, userData) {
        const user = await UserModel.findById(userId);
        if (!user) {
            throw new Error('존재하지 않는 사용자입니다.');
        }

        return await UserModel.updateUser(userId, {
            name: userData.name,
            phone: userData.phone
        });
    }

    // 비밀번호 변경
    async updatePassword(userId, currentPassword, newPassword) {
        const user = await UserModel.findById(userId);
        if (!user) {
            throw new Error('존재하지 않는 사용자입니다.');
        }

        // 현재 비밀번호 확인
        const currentHashedPassword = await this.hashPassword(currentPassword, user.salt);
        if (currentHashedPassword !== user.password) {
            throw new Error('현재 비밀번호가 일치하지 않습니다.');
        }

        // 새 비밀번호 해시화
        const newSalt = crypto.randomBytes(32).toString('hex');
        const newHashedPassword = await this.hashPassword(newPassword, newSalt);

        // 비밀번호 업데이트
        return await UserModel.updatePassword(userId, newHashedPassword, newSalt);
    }

    // 이메일 인증 확인
    async verifyEmail(userId, token) {
        const emailCode = await EmailCodeModel.verifyCode(userId, token);
        if (!emailCode) {
            throw new Error('유효하지 않은 인증 링크입니다.');
        }

        // 사용자 상태 업데이트 (인증 완료)
        await UserModel.updateStatus(userId, 0);
        
        // 사용된 인증 코드 삭제
        await EmailCodeModel.deleteCode(userId);

        return true;
    }
}

module.exports = new UserService();
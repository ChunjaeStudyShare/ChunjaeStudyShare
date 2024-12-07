const crypto = require('crypto');
const UserModel = require('../models/user.model');
const EmailCodeModel = require('../models/emailcode.model');
const errorMessage = require('../errormessage/error.message');
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
            throw new Error(errorMessage.DUPLICATE_ID);
        }

        // 이메일 중복 검사
        const existingEmail = await UserModel.findByEmail(userData.email);
        if (existingEmail) {
            throw new Error(errorMessage.DUPLICATE_EMAIL);
        }
        // 전화번호 중복 검사
        const existingPhone = await UserModel.findByPhone(userData.phone);
        if (existingPhone) {
            throw new Error(errorMessage.DUPLICATE_PHONE);
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
            throw new Error(errorMessage.INVALID_USER);
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
            throw new Error(errorMessage.INVALID_USER);
        }

        // 현재 비밀번호 확인
        const currentHashedPassword = await this.hashPassword(currentPassword, user.salt);
        if (currentHashedPassword !== user.password) {
            throw new Error(errorMessage.INVALID_PASSWORD);
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
            const user = await UserModel.findById(userId);
            // 이미 인증된 사용자인 경우
            if (user.status === 0) {
                throw new Error(errorMessage.EMAIL_VERIFICATION_ALREADY_VERIFIED);
            }
            // 인증 코드가 유효하지 않은 경우
            throw new Error(errorMessage.INVALID_EMAIL_VERIFICATION);
        }
        
        // 인증코드 날짜 확인 24시간 이후 만료
        const currentTime = new Date();
        const codeTime = new Date(emailCode.createdAt);
        const timeDifference = currentTime - codeTime;
        if (timeDifference > 1000 * 60 * 60 * 24) {
            throw new Error(errorMessage.EMAIL_VERIFICATION_EXPIRED);
        }

        // 사용자 상태 업데이트 (인증 완료)
        await UserModel.updateStatus(userId, 0);
        
        // 사용된 인증 코드 삭제
        await EmailCodeModel.deleteCode(userId);

        return true;
    }
}

module.exports = new UserService();
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
    async updatePassword(userId, newPassword) {
        const user = await UserModel.findById(userId);
        if (!user) {
            throw new Error(errorMessage.INVALID_USER);
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

    // 아이디 중복 체크
    async checkId(userId) {
        const existingUser = await UserModel.checkId(userId);
        return existingUser ? true : false;
    }

    // 이메일 중복 체크
    async checkEmail(email) {
        const existingEmail = await UserModel.checkEmail(email);
        return existingEmail ? true : false;
    } 

    async findByEmail(email) {
        const user = await UserModel.findByEmail(email);
        if (!user) {
            throw new Error(errorMessage.USER_NOT_FOUND);
        }
        return user;
    }

    // 비밀번호 재설정
    async resetPassword(userId, token, newPassword) {
        // 1. 토큰 검증
        const emailCode = await EmailCodeModel.verifyCode(userId, token);
        if (!emailCode) {
            throw new Error(errorMessage.INVALID_EMAIL_VERIFICATION);
        }

        // 2. 토큰 만료 시간 체크 (1시간)
        const currentTime = new Date();
        const codeTime = new Date(emailCode.createdAt);
        const timeDifference = currentTime - codeTime;
        if (timeDifference > 1000 * 60 * 60) { // 1시간
            throw new Error(errorMessage.TOKEN_EXPIRED);
        }

        // 3. 새 비밀번호 해시화
        const newSalt = crypto.randomBytes(32).toString('hex');
        const hashedPassword = await this.hashPassword(newPassword, newSalt);

        // 4. 비밀번호 업데이트
        await UserModel.updatePassword(userId, hashedPassword, newSalt);
        
        // 5. 사용된 토큰 삭제
        await EmailCodeModel.deleteCode(userId);

        return true;
    }

    // 전화번호 변경
    async updatePhone(userId, phone) {
        return await UserModel.updatePhone(userId, phone);
    }
}

module.exports = new UserService();
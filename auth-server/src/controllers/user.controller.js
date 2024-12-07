const UserService = require('../services/user.service');
const EmailService = require('../services/email.service');
const validator = require('../validators/validator');

class UserController {
    // 회원가입
    async register(req, res) {
        try {
            const { error } = validator.register.validate(req.body);
            if (error) {
                return res.status(400).json({
                    success: false,
                    message: error.details[0].message
                });
            }

            const userId = await UserService.register(req.body);
            await EmailService.sendVerificationEmail(userId, req.body.email);

            res.status(201).json({
                success: true,
                message: '회원가입이 완료되었습니다. 이메일 인증을 진행해주세요.'
            });
        } catch (error) {
            res.status(400).json({
                success: false,
                message: error.message
            });
        }
    }

    // 회원정보 수정
    async updateUser(req, res) {
        try {
            const userId = req.user.id;  // JWT 미들웨어에서 추가된 user 객체
            await UserService.updateUser(userId, req.body);

            res.json({
                success: true,
                message: '회원정보가 수정되었습니다.'
            });
        } catch (error) {
            res.status(400).json({
                success: false,
                message: error.message
            });
        }
    }

    // 비밀번호 변경
    async updatePassword(req, res) {
        try {
            const { error } = validator.updatePassword.validate(req.body);
            if (error) {
                return res.status(400).json({
                    success: false,
                    message: error.details[0].message
                });
            }

            const userId = req.user.id;
            const { currentPassword, newPassword } = req.body;
            await UserService.updatePassword(userId, currentPassword, newPassword);

            res.json({
                success: true,
                message: '비밀번호가 변경되었습니다.'
            });
        } catch (error) {
            res.status(400).json({
                success: false,
                message: error.message
            });
        }
    }

    // 이메일 인증
    async verifyEmail(req, res) {
        try {
            const { userId, token } = req.query;
            await UserService.verifyEmail(userId, token);
            
            // 인증 성공 시 메인 페이지로 리다이렉트
            res.redirect('https://www.gyeongminiya.asia/main');
        } catch (error) {
            // 인증 실패 시 로그인 페이지로 리다이렉트
            res.redirect('https://www.gyeongminiya.asia/login');
        }
    }

    // 비밀번호 재설정 메일 발송
    async sendPasswordResetEmail(req, res) {
        try {
            const { email } = req.body;
            const user = await UserService.findByEmail(email);
            if (!user) {
                throw new Error('등록되지 않은 이메일입니다.');
            }

            await EmailService.sendPasswordResetEmail(user.userId, email);

            res.json({
                success: true,
                message: '비밀번호 재설정 메일이 발송되었습니다.'
            });
        } catch (error) {
            res.status(400).json({
                success: false,
                message: error.message
            });
        }
    }

    // 아이디 중복 체크
    async checkId(req, res) {
        try {
            const { userId } = req.query;
            const isDuplicate = await UserService.checkId(userId);

            res.json({
                success: true,
                isDuplicate : isDuplicate
            });
        } catch (error) {
            res.status(400).json({
                success: false,
                message: error.message
            });
        }
    }

    // 이메일 중복 체크
    async checkEmail(req, res) {
        try {
            const { email } = req.query;
            const isDuplicate = await UserService.checkEmail(email);

            res.json({
                success: true,
                isDuplicate : isDuplicate
            });
        } catch (error) {
            res.status(400).json({
                success: false,
                message: error.message
            });
        }
    }
}

module.exports = new UserController();
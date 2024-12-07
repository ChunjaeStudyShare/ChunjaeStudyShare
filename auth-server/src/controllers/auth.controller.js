const AuthService = require('../services/auth.service');
const validator = require('../validators/validator');

class AuthController {
    // 로그인
    async login(req, res) {
        try {
            const { error } = validator.login.validate(req.body);
            console.log(req.body.userId);
            console.log(req.body.password);
            console.log(req.body.rememberMe);
            if (error) {
                return res.status(400).json({
                    success: false,
                    message: error.details[0].message
                });
            }

            const { userId, password, rememberMe } = req.body;
            const result = await AuthService.login(userId, password, rememberMe);

            res.json({
                success: true,
                data: result
            });
        } catch (error) {
            res.status(401).json({
                success: false,
                message: error.message
            });
        }
    }

    // 로그아웃
    async logout(req, res) {
        try {
            const userId = req.user.id;  // JWT 미들웨어에서 추가된 user 객체
            await AuthService.logout(userId);
            
            res.json({
                success: true,
                message: '로그아웃 되었습니다.'
            });
        } catch (error) {
            res.status(500).json({
                success: false,
                message: error.message
            });
        }
    }
}

module.exports = new AuthController();
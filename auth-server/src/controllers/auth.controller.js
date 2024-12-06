const AuthService = require('../services/auth.service');

class AuthController {
    constructor() {
        this.authService = new AuthService();
    }

    async login(req, res, next) {
        try {
            const { id, password, rememberMe } = req.body;
            const result = await this.authService.login(id, password, rememberMe);
            
            res.json({
                success: true,
                message: '로그인 성공',
                data: result
            });
        } catch (error) {
            next(error);
        }
    }
}

module.exports = new AuthController();
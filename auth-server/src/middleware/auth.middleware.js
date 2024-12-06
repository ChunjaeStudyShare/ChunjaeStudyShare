const TokenUtil = require('../util/token.util');
const errorMessage = require('../errormessage/error.message');

exports.verifyToken = (req, res, next) => {
    const authHeader = req.headers.authorization;
    
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({
            success: false,
            message: errorMessage.INVALID_TOKEN,
            error: 'INVALID_TOKEN'
        });
    }

    const token = authHeader.split(' ')[1];

    try {
        // 기존 token.util의 checkAll 메서드 사용
        TokenUtil.checkAll(token, req.params.userId);
        const decoded = TokenUtil.checkExpired(token);
        req.user = decoded;
        next();
    } catch (error) {
        let message = errorMessage.INVALID_TOKEN;
        let errorCode = 'INVALID_TOKEN';
        
        if (error.name === 'TokenExpiredError') {
            message = errorMessage.EXPIRED_TOKEN;
            errorCode = 'EXPIRED_TOKEN';
        }
        
        return res.status(401).json({
            success: false,
            message,
            error: errorCode
        });
    }
};
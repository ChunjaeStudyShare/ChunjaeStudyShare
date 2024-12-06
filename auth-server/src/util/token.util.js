const jwt = require('jsonwebtoken');

module.exports = {
    // 토큰 발급
    generateToken: (user) => jwt.sign({
        userId: user.id,
        email: user.email,
        name: user.name,
    }, process.env.JWT_SECRET, { expiresIn: '1h' }),

    // 토큰 권한 체크
    checkAuthority: (token, userId) => {
        if (token.userId !== userId) {
            throw new Error('권한이 없습니다.');
        } else {
            return true;
        }
    },

    // 토큰 만료 체크
    checkExpired: (token) => {
        return jwt.verify(token, process.env.JWT_SECRET);
    }
};

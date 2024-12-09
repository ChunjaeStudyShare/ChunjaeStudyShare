const responseTime = (req, res, next) => {
    // 개발 환경에서만 실행
    if (process.env.NODE_ENV === 'development') {
        const start = process.hrtime();
        res.on('finish', () => {
            const diff = process.hrtime(start);
            const time = diff[0] * 1e3 + diff[1] * 1e-6;
            console.log(`${req.method} ${req.originalUrl} - ${time.toFixed(2)}ms`);
        });
    }
    next();
};

module.exports = { responseTime }; 
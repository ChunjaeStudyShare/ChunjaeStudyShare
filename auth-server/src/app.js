const express = require('express');
const cors = require('cors');
const cookieParser = require('cookie-parser');
require('dotenv').config();

const authRoutes = require('./routes/auth.routes');
const userRoutes = require('./routes/user.routes');
// const testRoutes = require('./routes/test.routes'); // 테스트 라우트
const { responseTime } = require('./middleware/performance.middleware');

const app = express();

// cors 설정
const corsOptions = {
    //www.gyeongminiya.asia 에서 오는 요청만 허용
    // origin: ['https://www.gyeongminiya.asia'],
    // 일단 모든 요청 허용
    origin: true,
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    allowedHeaders: ['Content-Type', 'Authorization'],
    exposedHeaders: ['Authorization'],
    credentials: true
};

app.use(cors(corsOptions));

// json 파싱
app.use(express.json());
// 쿠키 파싱
app.use(cookieParser());

// 라우트   
app.use('/api/auth', authRoutes);
app.use('/api/user', userRoutes);
// app.use('/api/test', testRoutes); // 테스트 라우트

app.use(responseTime);

// 기본 에러 핸들러
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({
        success: false,
        message: err.message,
        errorCode: err.errorCode
    });
});

// 서버 실행
const PORT = process.env.PORT || 3443;
app.listen(PORT, () => {
    console.log(`서버가 ${PORT}번 포트에서 실행 중입니다.`);
});
const mysql = require('mysql2/promise');
const path = require('path');
require('dotenv').config({ path: path.join(__dirname, '../../.env') });

// 환경 변수 확인을 위한 로그
console.log('Database Config:', {
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    database: process.env.DB_NAME
});

const pool = mysql.createPool({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0
});

// 쿼리 실행 시간 측정을 위한 래퍼 함수
const executeQuery = async (query, values) => {
    const start = process.hrtime();
    
    // 개발 환경에서만 쿼리 로깅
    // if (process.env.NODE_ENV === 'development') {
    //     console.log('Starting query:', query);
    // }
    
    try {
        const result = await pool.query(query, values);
        const diff = process.hrtime(start);
        const time = diff[0] * 1e3 + diff[1] * 1e-6;
        
        // 개발 환경에서만 실행 시간 로깅
        // if (process.env.NODE_ENV === 'development') {
        //     console.log(`Query completed in ${time.toFixed(2)}ms`);
        // }
        
        return result;
    } catch (error) {
        console.error('Database error occurred');
        console.error('Query failed:', error);
        throw error;
    }
};

// pool 객체를 확장하여 실행 시간 측정 기능 추가
const poolWithTiming = {
    ...pool,
    query: executeQuery
};

module.exports = poolWithTiming;
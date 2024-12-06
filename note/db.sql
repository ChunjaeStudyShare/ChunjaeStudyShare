create database studyShare;

use studyShare;

create table Member (
    id varchar(255) primary key comment '유저 아이디' primary key,
    name varchar(20) not null comment '유저 이름',
    salt varchar(255) not null comment '유저 솔트',
    email varchar(255) not null comment '유저 이메일' unique,
    password varchar(255) not null comment '유저 비밀번호',
    phone varchar(11) not null comment '유저 전화번호' unique
);

create table Post (
    id int auto_increment primary key comment '게시글 아이디',
    title varchar(255) not null comment '제목',
    content text not null comment '내용',
    userId varchar(255) not null comment '작성자',
    todayType tinyint not null comment '오늘의 학습 타입(0: 비공개, 1: 공개)',
    shareType tinyint not null comment '공유 타입(0: 비공개, 1: 공개)',
    displayAt datetime not null comment '학습 노출 시작일 (날짜 및 시간)',
    displayEnd datetime not null comment '학습 노출 종료일 (날짜 및 시간)',
    createdAt datetime not null comment '게시글 생성일',
    domain varchar(255) not null comment '분야',
    hashtag varchar(255) not null comment '해시태그',
    foreign key (userId) references Member(id)
);

create table File (
    id int auto_increment primary key comment '파일 아이디',
    postId int not null comment '게시글 아이디',
    fileName varchar(255) not null comment '원본 파일 이름',
    path varchar(255) not null comment '파일 경로',
    foreign key (postId) references Post(id)
);

create table Friend (
    friendId varchar(255) not null comment '친구 아이디',
    requesterId varchar(255) not null comment '요청한 사람 아이디',
    status tinyint not null comment '친구 요청 상태 (0: 대기, 1: 수락)',
    foreign key (friendId) references Member(id),
    foreign key (requesterId) references Member(id)
);

create table ThumbsUp (
    userId varchar(255) not null comment '유저 아이디',
    postId int not null comment '게시글 아이디',
    foreign key (userId) references Member(id),
    foreign key (postId) references Post(id)
);

create table ChatRoom (
    id int auto_increment primary key comment '채팅방 아이디',
    senderId varchar(255) not null comment '보낸 사람 아이디',
    receiverId varchar(255) not null comment '받는 사람 아이디',
    senderStatus tinyint not null comment '보낸 사람 상태(0: 나감, 1: 채팅중)',
    receiverStatus tinyint not null comment '받는 사람 상태(0: 나감, 1: 채팅중)',
    foreign key (senderId) references Member(id),
    foreign key (receiverId) references Member(id)
);

create table ChatMessage (
    id int auto_increment primary key comment '채팅 메시지 아이디',
    chatRoomId int not null comment '채팅방 아이디',
    senderId varchar(255) not null comment '보낸 사람 아이디',
    message text not null comment '메시지',
    createdAt datetime not null comment '메시지 생성일',
    isRead tinyint not null comment '메시지 읽음 여부(0: 읽지 않음, 1: 읽음)',
    foreign key (chatRoomId) references ChatRoom(id),
    foreign key (senderId) references Member(id)
);

create table Share (
    id int auto_increment primary key comment '공유 인덱스',
    userId varchar(255) not null comment '유저 아이디(공유 받은 사람)',
    postId int not null comment '게시글 아이디',
    createdAt datetime not null comment '공유 일자',
    foreign key (userId) references Member(id),
    foreign key (postId) references Post(id)
);

create table Admin (
    id varchar(255) primary key comment '관리자 아이디',
    salt varchar(255) not null comment '관리자 솔트',
    password varchar(255) not null comment '관리자 비밀번호'
);

create table QnA (
    id int auto_increment primary key comment '질문 아이디',
    questionerId varchar(255) not null comment '질문자 아이디',
    question text not null comment '질문',
    answer text null comment '답변',
    foreign key (questionerId) references Member(id)
);
package net.fullstack7.studyShare.chat;

public class MessageContent {
    private String content;
    private String sender;

    public MessageContent(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    public MessageContent(String content) {
        this.content = content;
    }

    public MessageContent() {
    }

    public String getContent() {
        return this.content;
    }
}

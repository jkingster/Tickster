package io.jking.tickster.object;

import net.dv8tion.jda.api.entities.Message;

public class MessageData {

    private final long authorId;
    private final String authorTag;
    private final String content;
    private final String timestamp;

    public MessageData(Message message) {
        this.authorId = message.getIdLong();
        this.authorTag = message.getAuthor().getAsTag();
        this.content = message.getContentDisplay();
        this.timestamp = message.getTimeCreated().toLocalDateTime().toString();
    }

    public long getAuthorId() {
        return authorId;
    }

    public String getAuthorTag() {
        return authorTag;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("<%s | %s> (%s) => %s", authorTag, authorId, timestamp, content);
    }
}

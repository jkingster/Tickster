package io.jking.untitled.data;

import net.dv8tion.jda.api.entities.Message;

public class MessageData {

    private final long messageId;
    private final long authorId;
    private final long channelId;

    private String originalContent;
    private String editedContent;

    public MessageData(Message message) {
        this.messageId = message.getIdLong();
        this.authorId = message.getAuthor().getIdLong();
        this.channelId = message.getChannel().getIdLong();
        this.originalContent = message.getContentDisplay();
    }

    public long getMessageId() {
        return messageId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public long getChannelId() {
        return channelId;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getEditedContent() {
        return editedContent;
    }

    public void setEditedContent(String editedContent) {
        this.editedContent = editedContent;
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "messageId=" + messageId +
                ", authorId=" + authorId +
                ", channelId=" + channelId +
                ", originalContent='" + originalContent + '\'' +
                ", editedContent='" + editedContent + '\'' +
                '}';
    }
}

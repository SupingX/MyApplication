package com.laputa.zeejp.lib_common.http.response;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedbackResponse implements Parcelable {
    private String id;
    private String username;
    private String askContent;
    private String feedBackContent;
    private String askTime;
    private String feedBackTime;
    private String feedBackName;
    private String feedBackUser;
    private String phone;
    private int status;

    protected FeedbackResponse(Parcel in) {
        id = in.readString();
        username = in.readString();
        askContent = in.readString();
        feedBackContent = in.readString();
        askTime = in.readString();
        feedBackTime = in.readString();
        feedBackName = in.readString();
        feedBackUser = in.readString();
        phone = in.readString();
        status = in.readInt();
    }

    public static final Creator<FeedbackResponse> CREATOR = new Creator<FeedbackResponse>() {
        @Override
        public FeedbackResponse createFromParcel(Parcel in) {
            return new FeedbackResponse(in);
        }

        @Override
        public FeedbackResponse[] newArray(int size) {
            return new FeedbackResponse[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAskContent() {
        return askContent;
    }

    public void setAskContent(String askContent) {
        this.askContent = askContent;
    }

    public String getFeedBackContent() {
        return feedBackContent;
    }

    public void setFeedBackContent(String feedBackContent) {
        this.feedBackContent = feedBackContent;
    }

    public String getAskTime() {
        return askTime;
    }

    public void setAskTime(String askTime) {
        this.askTime = askTime;
    }

    public String getFeedBackTime() {
        return feedBackTime;
    }

    public void setFeedBackTime(String feedBackTime) {
        this.feedBackTime = feedBackTime;
    }

    public String getFeedBackName() {
        return feedBackName;
    }

    public void setFeedBackName(String feedBackName) {
        this.feedBackName = feedBackName;
    }

    public String getFeedBackUser() {
        return feedBackUser;
    }

    public void setFeedBackUser(String feedBackUser) {
        this.feedBackUser = feedBackUser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(askContent);
        dest.writeString(feedBackContent);
        dest.writeString(askTime);
        dest.writeString(feedBackTime);
        dest.writeString(feedBackName);
        dest.writeString(feedBackUser);
        dest.writeString(phone);
        dest.writeInt(status);
    }
}

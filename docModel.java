package com.example.docverifier.Model;

public class docModel {
    String docId, userID, docName, document, verifier, verified, reason;

    public docModel() {
    }

    public docModel(String docId, String userID, String docName, String document, String verifier, String verified, String reason) {
        this.docId = docId;
        this.userID = userID;
        this.docName = docName;
        this.document = document;
        this.verifier = verifier;
        this.verified = verified;
        this.reason = reason;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

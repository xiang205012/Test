package com.gordon.rrod.Sample.data;

/**
 * Created by Steven on 16/3/28.
 */
public class HttpResultEntity<T> {
    public String sessionId;
    public HttpResultDtoEntity<T> dto;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public HttpResultDtoEntity<T> getDto() {
        return dto;
    }

    public void setDto(HttpResultDtoEntity<T> dto) {
        this.dto = dto;
    }
}

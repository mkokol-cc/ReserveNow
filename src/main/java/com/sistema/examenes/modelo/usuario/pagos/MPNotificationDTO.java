package com.sistema.examenes.modelo.usuario.pagos;

public class MPNotificationDTO {
	private String action;
    private String apiVersion;
    private String applicationId;
    private String dateCreated;
    private String id;
    private String liveMode;
    private String type;
	private String userId;
    private TestData data;
    
    public MPNotificationDTO() {
    	
    }
    
    public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLiveMode() {
		return liveMode;
	}
	public void setLiveMode(String liveMode) {
		this.liveMode = liveMode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public TestData getData() {
		return data;
	}
	public void setData(TestData data) {
		this.data = data;
	}
	
	/*INNER CLASS*/
    public static class TestData {
        private Long id;
        public TestData() {
        }
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
    }
}

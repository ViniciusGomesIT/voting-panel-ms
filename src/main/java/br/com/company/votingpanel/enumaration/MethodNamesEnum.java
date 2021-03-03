package br.com.company.votingpanel.enumaration;

public enum MethodNamesEnum {

	CREATE("create"),
	VOTE("vote"),
	OPEN_SESSION("openSession");
	
	private String methodName;

	private MethodNamesEnum(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}
}

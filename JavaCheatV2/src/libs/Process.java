package libs;

public class Process {
	
	private int pid;
	private String name;
	
	public Process(final int pid, final String name) {
		this.pid = pid;
		this.name = name;
	}
	
	public void setPid(final int pid) {
		this.pid = pid;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public int getPid() {
		return pid;
	}
	
	public String getName() {
		return name;
	}

}

package libs;

public abstract class Constants {
	
	public static int BYTE = 0;
	public static int SHORT = 1;
	public static int INT = 2;
	public static int LONG = 3;
	public static int FLOAT = 4;
	public static int DOUBLE = 5;
	public static int CHAR = 7;
	
	/**
	 * Initial scan possible
	 */
	public static int EXACT_VALUE = 100;
	public static int SMALLER_THEN = 101;
	public static int BIGGER_THEN = 102;
	public static int BETWEEN = 103;
	public static int UNKOWN_VALUE = 104;
	
	/**
	 * Only comparable scan possible
	 */
	public static int INCREASED_VALUE = 105;
	public static int INCREASED_VALUE_BY = 106;
	public static int DECREASED_VALUE = 107;
	public static int DECREASED_VALUE_BY = 108;
	public static int CHANGED_VALUE = 109;
	public static int UNCHANGED_VALUE = 110;
	
	/**
	 * Event types
	 */
	public static int EVENT_UPDATE = 1001;
	public static int EVENT_LOCK = 1002;
	
	/**
	 * Event targets
	 */
	public static int TARGET_RANGE_FILTER = 10001;
	public static int TARGET_COMBOBOX_VALUETYPE = 10002;
	public static int TARGET_COMBOBOX_SCANTYPE = 10003;
	public static int TARGET_LABEL_PROCESS = 10004;
	public static int TARGET_LABEL_RESULTS = 10005;
	public static int TARGET_TABLE_SEARCH_RESULTS = 10006;
	
	

	public Constants() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getByteLength(final int pType) {
		switch (pType) {
			case 0:
				return 1;
			case 1:
				return 2;
			case 2:
				return 4;
			case 3:
				return 8;
			case 4:
				return 8;
			case 5:
				return 16;
			case 7:
				return 2;
			default:
				throw new IllegalArgumentException("Constant '" + pType + "' is unkown!");
		}
	}

}

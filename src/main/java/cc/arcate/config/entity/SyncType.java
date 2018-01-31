package cc.arcate.config.entity;

/**
 * Created by ACat on 26/01/2018.
 * ACat i lele.
 */
public enum SyncType {

	ADDITIONAL_SYNC, FORCE_SYNC;

	private static final String TYPE_ADDITIONNAL_SYNC = "additional";
	private static final String TYPE_FORCE_SYNC = "force";

	public static SyncType getSyncType(String type) {
		if (type == null)
			return FORCE_SYNC;
		switch (type) {
			case TYPE_ADDITIONNAL_SYNC:
				return ADDITIONAL_SYNC;
			case TYPE_FORCE_SYNC:
				return FORCE_SYNC;
			default:
				return FORCE_SYNC;
		}
	}
}

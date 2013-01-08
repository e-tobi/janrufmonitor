package de.janrufmonitor.framework.event;


/**
 *  This interface contains several constants used by the eventing mechanism
 *  of the framework.
 * 
 *@author     Thilo Brandt
 *@created    2003/08/24
 */
public interface IEventConst {

	/**
	 * Event type for unknown events.
	 */
    public final static int EVENT_TYPE_UNKNOWN = 0;
    
	/**
	 * Event type for application ready events.
	 */
	public final static int EVENT_TYPE_APPLICATION_READY = 10;
	
	/**
	 * Event type for updated call managers.
	 */
	public final static int EVENT_TYPE_CALL_MANAGER_UPDATED = 20;
	
	/**
	 * Event type for updated caller managers.
	 */
	public final static int EVENT_TYPE_CALLER_MANAGER_UPDATED = 30;

	/**
	 * Event type for incoming calls which are not identified yet.
	 */
    public final static int EVENT_TYPE_INCOMINGCALL = 100;

	/**
	 * Event type for rejecting calls.
	 */
    public final static int EVENT_TYPE_CALLREJECTED = 101;

	/**
	 * Event type for normal call clearing.
	 */
    public final static int EVENT_TYPE_CALLCLEARED = 102;

	/**
	 * Event type for retrieving full identified calls.
	 */
    public final static int EVENT_TYPE_IDENTIFIED_CALL = 103;

    /**
	 * Event type for incoming info events.
	 */
    public final static int EVENT_TYPE_INCOMING_INFO = 105;

	/**
	 * Event type for updated call information.
	 */
    public final static int EVENT_TYPE_UPDATE_CALL = 106;

	/**
	 * Event type for automatic call accepting, e.g., rejecting
	 */
    public final static int EVENT_TYPE_CALLACCEPTED = 107;

	/**
	 * Event type for manual call accepting, e.g., rejecting
	 */
    public final static int EVENT_TYPE_MANUALCALLACCEPTED = 108;

	/**
	 * Event type for outgoing calls which are not identified yet.
	 */
    public final static int EVENT_TYPE_OUTGOINGCALL = 109;
    
	/**
	 * Event type for retrieving full identified outgoing calls.
	 */
    public final static int EVENT_TYPE_IDENTIFIED_OUTGOING_CALL = 110;
    
	/**
	 * Event type for retrieving full identified outgoing accepted calls.
	 */
    public final static int EVENT_TYPE_IDENTIFIED_OUTGOING_CALL_ACCEPTED = 111;
    
	/**
	 * Event type for returning from hibernate mode.
	 */
    public final static int EVENT_TYPE_RETURNED_HIBERNATE = 201;
}

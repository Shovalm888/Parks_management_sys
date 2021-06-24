package enums;

/**
 * this class holds the different type of commands we have in our system.
 * 
 * @author group 20
 * @version 1.0
 */
public enum Commands {
	
	CheckID, RetCheckIDObj,

	CheckOnlineTraveler, RetCheckOnlineTraveler, RemoveOnlineTraveler, RetRemoveOnlineTraveler,

	UpdateOnlineWorker,

	attribute,

	GetTravelerInfoByID, GetTravelerInfoBySubscription, GetTravelerInfoByOrderID, RetTravelerInfoByID,
	RetTravelerInfoBySubscription, RetTravelerInfoByOrderID,

	GetTravelerOrders, CancelTravelerOrder, RetTravelerOrders, RetCancelTravelerOrder,

	CreateNewOrder, CheckNewOrder, RetCreateNewOrder, RetCheckNewOrder,

	GetTravelerMessages, RemoveTravelerMessage, RetTravelerMessages, RetRemoveTravelerMessage,

	CreateNewSingleTravelerOrder, RetNewSingleTravelerOrder,

	CreateNewFamilyOrder, RetNewFamilyOrder,

	CreateGroupNewOrder, RetNewGroupOrder,

	CheckOrderDate, RetOrderDate,

	GetOrderQueue, RetOrderQueue,

	GetParkInfo, RetParkInfo,

	GetParkEvents, RetParkEvents,

	GetReport, RetReport,

	GetRequests, RetRequests,

	GetPermissions, RetPermissions,

	GetParkTicketPrice, RetParkTicketPrice,

	GetWorkerInfo, RetWorkerInfo,

	GetParkActivityDays, RetParkActivityDays, AddNewTraveler, RetNewTraveler,

	GetOrderTotalCost, RetGetOrderTotalCost,

	GetParkTimeOfStay, RetGetParkTimeOfStay,

	AddToQueue, RetAddToQueue, GetAvailableOrderDates, RetGetAvailableOrderDates, GetSubscriptionTravelerByID,
	RetGetSubscriptionTravelerByID,

	GetParkCapacityNumber, RetGetParkCapacityNumber, GetTravelerInfo, RetGetTravelerInfo, RetUpdateTravelerType,
	UpdateTravelerType, UpdateTravelerInfo, RetUpdateTravelerInfo, CreateNewSubscription, RetCreateNewSubscription,
	CreateNewTraveler, RetCreateNewTraveler, CreateNewOrderCasual, RetCreateNewOrderCasual, CheckParkParameterUpdate,
	RetCheckParkParameterUpdate, CreateNewRequest, RetCreateNewRequest, CheckEvent, RetCheckEvent, UpdateParkParameter,
	RetUpdateParkParameter, GetEventByRequestID, RetGetEventByRequestID, GetReportOrders, RetGetReportOrders,
	GetCapacityNumber, RetGetCapacityNumber, RetGetParkInfo, ParkWorkerCreateNewOrder, RetParkWorkerCreateNewOrder,
	CheckForEvent, RetCheckForEvent, WorkerCheckNewOrder, RetWorkerCheckNewOrder, CheckOnlineWorker, RemoveOnlineWorker,
	RetCheckOnlineWorker, RetRemoveOnlineWorker, CheckOccupancy, RetCheckOccupancy, CheckIfOrderInQueue,
	RetCheckIfOrderInQueue, RetGetUpcomingEvents, GetUpcomingEvents;
}

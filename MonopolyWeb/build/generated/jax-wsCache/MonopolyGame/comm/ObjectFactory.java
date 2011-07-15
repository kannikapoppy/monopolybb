
package comm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the comm package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ResignResponseReturn_QNAME = new QName("http://monopoly", "return");
    private final static QName _JoinGameGameName_QNAME = new QName("http://monopoly", "gameName");
    private final static QName _JoinGamePlayerName_QNAME = new QName("http://monopoly", "playerName");
    private final static QName _EventEventMessage_QNAME = new QName("http://monopoly/xsd", "eventMessage");
    private final static QName _EventPlayerName_QNAME = new QName("http://monopoly/xsd", "playerName");
    private final static QName _EventPaymentToPlayerName_QNAME = new QName("http://monopoly/xsd", "paymentToPlayerName");
    private final static QName _EventGameName_QNAME = new QName("http://monopoly/xsd", "gameName");
    private final static QName _MonopolyResultErrorMessage_QNAME = new QName("http://results.monopoly/xsd", "errorMessage");
    private final static QName _GameDetailsResultStatus_QNAME = new QName("http://results.monopoly/xsd", "status");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: comm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResignResponse }
     * 
     */
    public ResignResponse createResignResponse() {
        return new ResignResponse();
    }

    /**
     * Create an instance of {@link GetGameBoardXMLResponse }
     * 
     */
    public GetGameBoardXMLResponse createGetGameBoardXMLResponse() {
        return new GetGameBoardXMLResponse();
    }

    /**
     * Create an instance of {@link StartGame }
     * 
     */
    public StartGame createStartGame() {
        return new StartGame();
    }

    /**
     * Create an instance of {@link SetDiceRollResults }
     * 
     */
    public SetDiceRollResults createSetDiceRollResults() {
        return new SetDiceRollResults();
    }

    /**
     * Create an instance of {@link SetDiceRollResultsResponse }
     * 
     */
    public SetDiceRollResultsResponse createSetDiceRollResultsResponse() {
        return new SetDiceRollResultsResponse();
    }

    /**
     * Create an instance of {@link BuyResponse }
     * 
     */
    public BuyResponse createBuyResponse() {
        return new BuyResponse();
    }

    /**
     * Create an instance of {@link Resign }
     * 
     */
    public Resign createResign() {
        return new Resign();
    }

    /**
     * Create an instance of {@link GetAllEvents }
     * 
     */
    public GetAllEvents createGetAllEvents() {
        return new GetAllEvents();
    }

    /**
     * Create an instance of {@link Event }
     * 
     */
    public Event createEvent() {
        return new Event();
    }

    /**
     * Create an instance of {@link GetWaitingGamesResponse }
     * 
     */
    public GetWaitingGamesResponse createGetWaitingGamesResponse() {
        return new GetWaitingGamesResponse();
    }

    /**
     * Create an instance of {@link PlayerDetailsResult }
     * 
     */
    public PlayerDetailsResult createPlayerDetailsResult() {
        return new PlayerDetailsResult();
    }

    /**
     * Create an instance of {@link EventArrayResult }
     * 
     */
    public EventArrayResult createEventArrayResult() {
        return new EventArrayResult();
    }

    /**
     * Create an instance of {@link GameDetailsResult }
     * 
     */
    public GameDetailsResult createGameDetailsResult() {
        return new GameDetailsResult();
    }

    /**
     * Create an instance of {@link IDResult }
     * 
     */
    public IDResult createIDResult() {
        return new IDResult();
    }

    /**
     * Create an instance of {@link JoinGame }
     * 
     */
    public JoinGame createJoinGame() {
        return new JoinGame();
    }

    /**
     * Create an instance of {@link GetGameBoardSchemaResponse }
     * 
     */
    public GetGameBoardSchemaResponse createGetGameBoardSchemaResponse() {
        return new GetGameBoardSchemaResponse();
    }

    /**
     * Create an instance of {@link GetActiveGamesResponse }
     * 
     */
    public GetActiveGamesResponse createGetActiveGamesResponse() {
        return new GetActiveGamesResponse();
    }

    /**
     * Create an instance of {@link GetPlayersDetailsResponse }
     * 
     */
    public GetPlayersDetailsResponse createGetPlayersDetailsResponse() {
        return new GetPlayersDetailsResponse();
    }

    /**
     * Create an instance of {@link GetGameDetails }
     * 
     */
    public GetGameDetails createGetGameDetails() {
        return new GetGameDetails();
    }

    /**
     * Create an instance of {@link JoinGameResponse }
     * 
     */
    public JoinGameResponse createJoinGameResponse() {
        return new JoinGameResponse();
    }

    /**
     * Create an instance of {@link Buy }
     * 
     */
    public Buy createBuy() {
        return new Buy();
    }

    /**
     * Create an instance of {@link StartGameResponse }
     * 
     */
    public StartGameResponse createStartGameResponse() {
        return new StartGameResponse();
    }

    /**
     * Create an instance of {@link GetGameDetailsResponse }
     * 
     */
    public GetGameDetailsResponse createGetGameDetailsResponse() {
        return new GetGameDetailsResponse();
    }

    /**
     * Create an instance of {@link GetPlayersDetails }
     * 
     */
    public GetPlayersDetails createGetPlayersDetails() {
        return new GetPlayersDetails();
    }

    /**
     * Create an instance of {@link MonopolyResult }
     * 
     */
    public MonopolyResult createMonopolyResult() {
        return new MonopolyResult();
    }

    /**
     * Create an instance of {@link GetAllEventsResponse }
     * 
     */
    public GetAllEventsResponse createGetAllEventsResponse() {
        return new GetAllEventsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MonopolyResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = ResignResponse.class)
    public JAXBElement<MonopolyResult> createResignResponseReturn(MonopolyResult value) {
        return new JAXBElement<MonopolyResult>(_ResignResponseReturn_QNAME, MonopolyResult.class, ResignResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = GetGameBoardXMLResponse.class)
    public JAXBElement<String> createGetGameBoardXMLResponseReturn(String value) {
        return new JAXBElement<String>(_ResignResponseReturn_QNAME, String.class, GetGameBoardXMLResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "gameName", scope = JoinGame.class)
    public JAXBElement<String> createJoinGameGameName(String value) {
        return new JAXBElement<String>(_JoinGameGameName_QNAME, String.class, JoinGame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "playerName", scope = JoinGame.class)
    public JAXBElement<String> createJoinGamePlayerName(String value) {
        return new JAXBElement<String>(_JoinGamePlayerName_QNAME, String.class, JoinGame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = GetGameBoardSchemaResponse.class)
    public JAXBElement<String> createGetGameBoardSchemaResponseReturn(String value) {
        return new JAXBElement<String>(_ResignResponseReturn_QNAME, String.class, GetGameBoardSchemaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "gameName", scope = StartGame.class)
    public JAXBElement<String> createStartGameGameName(String value) {
        return new JAXBElement<String>(_JoinGameGameName_QNAME, String.class, StartGame.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "gameName", scope = GetGameDetails.class)
    public JAXBElement<String> createGetGameDetailsGameName(String value) {
        return new JAXBElement<String>(_JoinGameGameName_QNAME, String.class, GetGameDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PlayerDetailsResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = GetPlayersDetailsResponse.class)
    public JAXBElement<PlayerDetailsResult> createGetPlayersDetailsResponseReturn(PlayerDetailsResult value) {
        return new JAXBElement<PlayerDetailsResult>(_ResignResponseReturn_QNAME, PlayerDetailsResult.class, GetPlayersDetailsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IDResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = JoinGameResponse.class)
    public JAXBElement<IDResult> createJoinGameResponseReturn(IDResult value) {
        return new JAXBElement<IDResult>(_ResignResponseReturn_QNAME, IDResult.class, JoinGameResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MonopolyResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = SetDiceRollResultsResponse.class)
    public JAXBElement<MonopolyResult> createSetDiceRollResultsResponseReturn(MonopolyResult value) {
        return new JAXBElement<MonopolyResult>(_ResignResponseReturn_QNAME, MonopolyResult.class, SetDiceRollResultsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MonopolyResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = BuyResponse.class)
    public JAXBElement<MonopolyResult> createBuyResponseReturn(MonopolyResult value) {
        return new JAXBElement<MonopolyResult>(_ResignResponseReturn_QNAME, MonopolyResult.class, BuyResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MonopolyResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = StartGameResponse.class)
    public JAXBElement<MonopolyResult> createStartGameResponseReturn(MonopolyResult value) {
        return new JAXBElement<MonopolyResult>(_ResignResponseReturn_QNAME, MonopolyResult.class, StartGameResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GameDetailsResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = GetGameDetailsResponse.class)
    public JAXBElement<GameDetailsResult> createGetGameDetailsResponseReturn(GameDetailsResult value) {
        return new JAXBElement<GameDetailsResult>(_ResignResponseReturn_QNAME, GameDetailsResult.class, GetGameDetailsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly/xsd", name = "eventMessage", scope = Event.class)
    public JAXBElement<String> createEventEventMessage(String value) {
        return new JAXBElement<String>(_EventEventMessage_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly/xsd", name = "playerName", scope = Event.class)
    public JAXBElement<String> createEventPlayerName(String value) {
        return new JAXBElement<String>(_EventPlayerName_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly/xsd", name = "paymentToPlayerName", scope = Event.class)
    public JAXBElement<String> createEventPaymentToPlayerName(String value) {
        return new JAXBElement<String>(_EventPaymentToPlayerName_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly/xsd", name = "gameName", scope = Event.class)
    public JAXBElement<String> createEventGameName(String value) {
        return new JAXBElement<String>(_EventGameName_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "gameName", scope = GetPlayersDetails.class)
    public JAXBElement<String> createGetPlayersDetailsGameName(String value) {
        return new JAXBElement<String>(_JoinGameGameName_QNAME, String.class, GetPlayersDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://results.monopoly/xsd", name = "errorMessage", scope = MonopolyResult.class)
    public JAXBElement<String> createMonopolyResultErrorMessage(String value) {
        return new JAXBElement<String>(_MonopolyResultErrorMessage_QNAME, String.class, MonopolyResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://results.monopoly/xsd", name = "status", scope = GameDetailsResult.class)
    public JAXBElement<String> createGameDetailsResultStatus(String value) {
        return new JAXBElement<String>(_GameDetailsResultStatus_QNAME, String.class, GameDetailsResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EventArrayResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://monopoly", name = "return", scope = GetAllEventsResponse.class)
    public JAXBElement<EventArrayResult> createGetAllEventsResponseReturn(EventArrayResult value) {
        return new JAXBElement<EventArrayResult>(_ResignResponseReturn_QNAME, EventArrayResult.class, GetAllEventsResponse.class, value);
    }

}

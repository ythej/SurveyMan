package edu.umass.cs.surveyman.survey;

import edu.umass.cs.surveyman.SurveyMan;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * Component subtype representing arbitrary HTML. Questions used to be a mix of HTMLComponents and
 * {@link edu.umass.cs.surveyman.survey.StringComponent}s when the RESOURCE column was in use. Now a question is one of
 * an HTMLComponent or {@link edu.umass.cs.surveyman.survey.StringComponent}.
 */
public class HTMLComponent extends Component {

    /**
     * The normalized HTML String associated with this Component.
     */
    public final String data;

    /**
     * Takes in a string of purported HTML, parses and normalizes it, and returns an HTMLComponent. The row and column
     * data is used for debugging and sorting.
     *
     * @param html Arbitrary HTML fragment.
     * @param row Source row.
     * @param col Source column.
     */
    public HTMLComponent(String html, int row, int col) {
        super(row, col);
        assert !html.isEmpty() : "Should not be calling the HTMLComponent constructor on an empty data string.";
        Document doc = Jsoup.parseBodyFragment(html).normalise();
        SurveyMan.LOGGER.debug(String.format("Input html: %s\n\tParsed HTML fragment: %s", html, doc.body().html()));
        this.data = doc.body().html();
    }

    public HTMLComponent(String html){
        this(html, Component.SYSTEM_DEFINED, Component.DEFAULT_SOURCE_COL);
    }

    public boolean isEmpty(){
        return this.data==null || this.getCid()==null;
    }

    @Override
    protected String jsonize() {
        if(data.isEmpty()) {
            throw new RuntimeException("AGAA");
        }
        return String.format("{ \"id\" : \"%s\", \"otext\" : \"%s\" }"
                , this.getCid()
                , data.replace("\"", "\\\""));
    }

    /**
     * Tests whether the input data is valid HTML using Jsoup.
     * @param data Potentially HTML data.
     * @return boolean indicating whether the input is valid HTML5.
     */
    public static boolean isHTMLComponent(String data){
        return !data.isEmpty() && Jsoup.isValid(data, Whitelist.basicWithImages());
    }

    /**
     * Two HTMLComponents are equal if they have equal component ids.
     * @param c Another HTMLComponent.
     * @return boolean indicating whether this and the input object have equivalent component ids.
     */
    @Override
    public boolean equals(Object c) {
        if (c instanceof HTMLComponent)
            return this.data.equals(((HTMLComponent) c).data)
                    && this.getCid().equals(((HTMLComponent) c).getCid());
        else return false; 
    }

    @Override
    public boolean dataEquals(String data) {
        return isHTMLComponent(data) && this.data.equals(data);
    }

    @Override
    public String toString() {
        return data;
    }

    /**
     * Returns the data contents of the HTMLComponent.
     * @param dataOnly boolean indicating whether to return data contents only.
     * @return String representation of the HTMLComponent.
     */
    public String toString(boolean dataOnly) {
        if (dataOnly)
            return this.data;
        else return this.toString();
    }

}
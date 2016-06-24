import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentClass;
import edu.stanford.nlp.util.CoreMap;





public class SentiTweets {

	public static void main(String[] args) {
		
		String tag="baseball";
		String urlToFetch="http://tweetprocessor-env.us-west-2.elasticbeanstalk.com/TweetSent.php?hash="+tag;
		
		try {
			URL url = new URL(urlToFetch);
		    HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
		    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String lineRead="";
		    lineRead=br.readLine();
		    JSONObject obj = new JSONObject(lineRead);
		    JSONArray jArr=obj.getJSONArray("statuses");
		    String[] statement= new String[jArr.length()];
		    String[] sentiment= new String[jArr.length()];
		    double total=0, positive=0, negative=0, neutral=0;
		    for(int i=0;i<jArr.length();i++){
		    	statement[i]=(String) jArr.getJSONObject(i).get("text");
		    	sentiment[i]=getSentiment(statement[i]);
		    	if(sentiment[i].contains("Very positive"))
		    		positive+=2;
		    	if(sentiment[i].contains("Very negative"))
		    		negative+=2;
		    	if(sentiment[i].contains("Positive"))
		    		positive++;
		    	if(sentiment[i].contains("Negative"))
		    		negative++;
		    	if(sentiment[i].contains("Neutral"))
		    		neutral++;
		    }
		    
		    total=positive+negative+neutral;
		    System.out.println(positive+" "+negative+" "+neutral+""+sentiment.length);
		    System.out.println("Positive Percentage"+(int)(positive/total*100));
		    System.out.println("Negative Percentage"+(int)(negative/total*100));
		    System.out.println("Neutral Percentage"+(int)(neutral/total*100));
		    	
		} catch (IOException e) {
			e.printStackTrace();
		}   

	}
	
	static String getSentiment(String text){
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(text);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		String sentiment="";
		for (CoreMap sentence : sentences) {
		  sentiment += sentence.get(SentimentClass.class);		  
		}
		return sentiment;
	}

}

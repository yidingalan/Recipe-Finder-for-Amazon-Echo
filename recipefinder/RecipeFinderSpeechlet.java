package recipefinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;


//Handle speechlet request

public class RecipeFinderSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(recipeFinderSpeechlet.class);

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    //When the skill gets launhed
    //when the user says "Alexa, start 'Recipe Finder' "
    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        String speechOutput = "Welcome to Receipe Finder. You can list all the ingredients you have at your kitchen,"
                + "and I will help you find the best recipe..."
                + "Now, what ingredients do you have?";

        //If the user does not reply or says something that is hard to understand
        String repromptText = "For instructions on what you can say, please say help me.";

        return newAskResponse(speechOutput, repromptText);

    }

    //When Alexa gets the input from the user -- "kale, beef, garlic"
    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        //If the user is giving the correct intent input
        if ("RecipeIntent".equals(intentName)) {
            return GetRecipeResponse(intent);
        }
        //If the user needs help
        else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelp();
        }
        //If the user says stop
        else if ("AMAZON.StopIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Bye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        }
        //If the user cancels the request
        else if ("AMAZON.CancelIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Bye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        }
        //Error handling for invalid input
        else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }


    /**
    * logic for getting the recipes
    * SpeechletResponse: defines the text to speak to the user
    */
    private SpeechletResponse GetRecipeResponse(Intent intent){


    }



    //When the user needs help
    private SpeechletResponse getHelp(){
        String speechOutput = "You only need to list all the items,"
            + "for example, you can say kale, beef, garlic,"
            + "Now, what do you have at your kitchen?";

        String repromptText = "Or you can just say one word kale,"
            + "or exit... Now, what do you have at your kitchen?";

        return newAskResponse(speechOutput, repromptText);
    }

    //Just for prompting user for response and reprompt if they don't answer
    private SpeechletResponse newAskResponse(String stringOutput, String repromptText) {
           PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
           outputSpeech.setText(stringOutput);

           PlainTextOutputSpeech repromptOutputSpeech = new PlainTextOutputSpeech();
           repromptOutputSpeech.setText(repromptText);
           Reprompt reprompt = new Reprompt();
           reprompt.setOutputSpeech(repromptOutputSpeech);

           return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
       }

package parsing.antlr;

import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hongdi.ren.
 */
public class MyErrorListener extends ConsoleErrorListener {

    private List<Exception> exceptionList = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
        exceptionList.add(e);
    }

    public List<Exception> getExceptionList() {
        return Collections.unmodifiableList(exceptionList);
    }
}

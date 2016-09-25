package servlet;

import QueryService.DataLoader;
import QueryService.QueryExecutor;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by sajit.m on 9/24/2016.
 */
public class QueryResult extends HttpServlet {
    private static Logger log = Logger.getLogger(QueryResult.class);
    private static final String QUERY = "query";

    @Override
    public void init() throws ServletException {
        super.init();
        DataLoader.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String query = request.getParameter(QUERY);
        List<String> documents = getDocumentsForQuery(query);
//        for(int i=0;i<documents.size();i++){
//            System.out.println(documents.get(i));
//        }
//        request.setAttribute("documents", documents);
//        RequestDispatcher rd = request.getRequestDispatcher("./WEB-INF/showDocs.jsp");
//        try {
//            rd.forward(request,response);
//        } catch (Exception e) {
//            log.error("unable to process request",e);
//        }
        response.getWriter().print(documents);
    }

    private List<String> getDocumentsForQuery(String query) {
        String[] tokens = query.split(",");
        return new QueryExecutor().getResult(tokens);
    }
}

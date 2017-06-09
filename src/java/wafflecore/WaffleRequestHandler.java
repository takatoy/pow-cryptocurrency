import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

class WaffleRequestHandler extends HttpServlet {
    public void getRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=Shift_JIS");

        String command = request.getParameter("command");
        String parameter = request.getParameter("parameter");

        switch (command) {
            case command:
                statements;
                break;
            default:
                break;
        }

        PrintWriter pw = response.getWriter();
        StringBuffer sb = new StringBuffer();

        pw.println(new String(sb));
        pw.close();
    }
}

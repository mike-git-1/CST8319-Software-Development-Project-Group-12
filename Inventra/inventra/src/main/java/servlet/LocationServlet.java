package servlet;
import database.dao.LocationDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Location;
import model.builders.LocationBuilder;
import java.io.IOException;

@WebServlet(urlPatterns = {"/locations/*"})
public class LocationServlet extends HttpServlet {
    private final LocationDAO locationDAO = new LocationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No location ID provided!");
            return;
        }

        int locationId;
        try {
            locationId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid location ID format!");
            return;
        }

        try {
            Location location = locationDAO.getLocationById(locationId);
            if (location == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Location not found!");
                return;
            }

            request.setAttribute("location", location);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/location.jsp"); // TODO: Create location.jsp
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Server error retrieving location!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int companyId = Integer.parseInt(request.getParameter("company_id"));
            String name = request.getParameter("name");
            String address1 = request.getParameter("address_1");
            String address2 = request.getParameter("address_2");

            Location location = new LocationBuilder()
                    .withCompanyId(companyId)
                    .withName(name)
                    .withAddress1(address1)
                    .withAddress2(address2)
                    .build();

            boolean success = locationDAO.insertLocation(location);
            response.getWriter().write(success ? "Location created!" : "Failed to create location!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Server error during location creation!");
        }
    }
}

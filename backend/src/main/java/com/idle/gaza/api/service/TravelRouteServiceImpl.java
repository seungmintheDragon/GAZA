package com.idle.gaza.api.service;

import com.idle.gaza.api.request.TravelRouteRequest;
import com.idle.gaza.api.response.ReviewResponse;
import com.idle.gaza.api.response.TravelRouteResponse;
import com.idle.gaza.db.entity.Reservation;
import com.idle.gaza.db.entity.Review;
import com.idle.gaza.db.entity.TravelRoute;
import com.idle.gaza.db.repository.ReservationRepository;
import com.idle.gaza.db.repository.TravelRouteRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@Service
public class TravelRouteServiceImpl implements TravelRouteService{

    @Autowired
    TravelRouteRepository travelRouteRepository;

    @Autowired
    ReservationRepository reservationRepository;


    @Override
    public Integer saveRoutes(List<TravelRouteRequest> routeInfo, int reservationId) throws Exception {
        // 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new NoSuchElementException("Reservation not found"));

        List<TravelRoute> list = new ArrayList<>();
        for(int i=0; i<routeInfo.size(); i++){
            // 루트 생성
            TravelRouteRequest req = routeInfo.get(i);
            TravelRoute route = new TravelRoute(
                    reservation,
                    req.getName(),
                    req.getAddress(),
                    i+1,
                    req.getLatitude(),
                    req.getLongitude()
            );
            list.add(route);
        }
        // 루트 저장
        travelRouteRepository.saveAll(list);

        return 1;
    }

    @Override
    public List<TravelRouteResponse> getRoutes(Integer reservationId) {
        List<TravelRoute> routes = travelRouteRepository.findByRerservationId_ReservationId(reservationId);
        List<TravelRouteResponse> routeRes = new ArrayList<>(routes.size());
        for(int i=0; i<routes.size(); i++){
            TravelRoute route = routes.get(i);
            TravelRouteResponse res = new TravelRouteResponse(
                    route.getName(),
                    route.getAddress(),
                    route.getOrderNo(),
                    route.getLatitude(),
                    route.getLongitude()
            );
            routeRes.add(res);
        }
        return routeRes;
    }


    @Override
    public List<TravelRouteResponse> getRouteByOrder(int reservationId) {
        List<TravelRoute> travelOrderList = travelRouteRepository.getRouteOrderList(reservationId);
        List<TravelRouteResponse> response = new ArrayList<>();

        for (TravelRoute route : travelOrderList) {
            System.out.println("route order = " + route.toString());
            TravelRouteResponse res = new TravelRouteResponse(                    route.getName(),
                    route.getAddress(),
                    route.getOrderNo(),
                    route.getLatitude(),
                    route.getLongitude());
            response.add(res);
        }

        return response;
    }

    public int excelDownload(int reservationId, HttpServletResponse response){

        List<TravelRouteResponse> routeOrderList = getRouteByOrder(reservationId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("tour_route");

        Row row = null;
        Cell cell = null;
        int rowNum = 0;
        int column = 0;

        //header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(column++);
        cell.setCellValue("Name");
        cell = row.createCell(column++);
        cell.setCellValue("Address");
        cell = row.createCell(column++);
        cell.setCellValue("Order");

        //body
        for (TravelRouteResponse res : routeOrderList){
            column = 0;
            row = sheet.createRow(rowNum++);
            cell = row.createCell(column++);
            cell.setCellValue(res.getName());
            cell = row.createCell(column++);
            cell.setCellValue(res.getAddress());
            cell = row.createCell(column++);
            cell.setCellValue(res.getOrder());
        }

        //cotent type, file name 설정
        response.setHeader("Content-Disposition", "attachment;filename=travelRoute.xlsx");
        response.setContentType("ms-vnd/excel");

        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("error", e);
            return 0;
        } finally {
            try{
                workbook.close();
            }catch (IOException e) {
                log.error("error", e);
                return 0;
            }
        }
        return 1;
    }

}

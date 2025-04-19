package ticket_booking.entities;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.sql.Time;
import java.util.List;
import java.util.Map;

public class Train {
    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, String> stationTimes;
    private List<String> stations;

    public String getTrainId() {

        return trainId;
    }

    public void setTrainId(String trainId) {

        this.trainId = trainId;
    }

    public List<List<Integer>> getSeats() {

        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {

        this.seats = seats;
    }

    public Map<String, String> getStationTimes() {

        return stationTimes;
    }

    public void setStationTimes(Map<String, String> stationTimes) {

        this.stationTimes = stationTimes;
    }

    public String getTrainNo() {

        return trainNo;
    }

    public void setTrainNo(String trainNo) {

        this.trainNo = trainNo;
    }

    public List<String> getStations() {

        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }
    public String getTrainInfo(){
        return String.format("Train ID: %s Train No: %s", trainId, trainNo);
    }
}

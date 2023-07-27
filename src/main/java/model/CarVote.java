package model;

import lombok.Data;
import lombok.NonNull;

@Data
public class CarVote {
    @NonNull
    private Car car;
    private int countVotes = 0;

    public void countVotesIncr() {
        this.countVotes++;
    }

}
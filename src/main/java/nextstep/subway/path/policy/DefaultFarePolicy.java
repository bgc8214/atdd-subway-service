package nextstep.subway.path.policy;

import org.springframework.stereotype.Component;

@Component
public class DefaultFarePolicy implements FarePolicy {
    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_ADDED_FARE = 100;
    private static final FareSection[] distanceTable = {FareSection.FIRST, FareSection.SECOND};

    @Override
    public int calculateFare(int extraFare, int distance) {
        return extraFare + accumulateFare(distanceTable.length - 1, distance, DEFAULT_FARE);
    }

    private int accumulateFare(int index, int distance, int fare) {
        if (index < 0) {
            return fare;
        }

        int overFare = 0;
        int overDistance = distance - distanceTable[index].getDistance();
        if  (overDistance > 0) {
            overFare = calculateOverFare(overDistance, distanceTable[index].getSection());
            distance -= overDistance;
        }

        return accumulateFare(index - 1, distance, fare + overFare);
    }

    private int calculateOverFare(int distance, int section) {
        return (int) ((Math.ceil((distance - 1) / section) + 1) * DEFAULT_ADDED_FARE);
    }

    enum FareSection {
        FIRST (10, 5),
        SECOND (50, 8);

        private final int distance;
        private final int section;

        FareSection(int distance, int section) {
            this.distance = distance;
            this.section = section;
        }

        public int getDistance() {
            return distance;
        }

        public int getSection() {
            return section;
        }
    }
}
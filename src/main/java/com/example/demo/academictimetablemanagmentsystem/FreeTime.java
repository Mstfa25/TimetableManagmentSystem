package com.example.demo.academictimetablemanagmentsystem;

import com.example.demo.database.connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FreeTime {

    //the 6-Days free time
    //0 is saturday 6 is friday
    public node dayStartEnd[];

    //this constructor make free Time without any free time
    public FreeTime() {
        dayStartEnd = new node[6];
        for (int i = 0; i < 6; i++) {
            dayStartEnd[i] = new node();
        }
    }

    //this constructor make all the time free Time
    // the var x dont have any function
    public FreeTime(int x) {
        dayStartEnd = new node[6];
        for (int i = 0; i < dayStartEnd.length; i++) {
            dayStartEnd[i] = new node();
            dayStartEnd[i].startSession = 8;
            dayStartEnd[i].endSession = 17;
        }

    }

    // this method add free time with the day and the start time and the end time for the free time
    //  and if the time was added before it will do nothing
    //  and it merges the free time if it is contious
    public void addFreeTime(int day, int start, int end) {
        //cheak if the no free time in the day was enterd it will
        //  set the free time in this day the free time was passed 

        node n = dayStartEnd[day];
        if (n.startSession == null) {
            n.startSession = start;
            n.endSession = (end);
            return;
            //if there is a free time and the last start is greater than of equal 
            //  the start time was enterd and the new start time is greater than or equal
            //  the last end time 
        } else if (start >= n.startSession
                && start <= n.endSession) {
            // if the last condition was true and the new end is lessthan
            //  or equal the new end it will do nothing as the new time is free before 
            if (end <= n.endSession) {
                return;
            } else {
                //else the last end time is greater then the new time it will update the new end time
                n.endSession = end;
                return;
            }

        } else if (start <= n.startSession
                && end >= n.startSession) {
            if (end > n.endSession) {
                n.startSession = start;
                n.endSession = end;
                return;
            } else {
                n.startSession = start;
                return;
            }
        }
        while (n.next != null) {
            if (start > n.startSession
                    && start <= n.endSession) {
                if (end < n.endSession) {
                    return;
                } else {
                    n.endSession = end;
                    return;
                }
            } else if (start < n.startSession
                    && end >= n.startSession) {
                if (end > n.endSession) {
                    n.startSession = start;
                    n.endSession = end;
                    return;
                } else {
                    n.startSession = start;
                    return;
                }
            }
            n = n.next;
        }
        n.next = new node();
        n.next.startSession = (start);
        n.next.endSession = (end);
        n.next.privous = n;
    }

    //will return an array of 6 days id the day is true this means
    //  that the day has free time
    public boolean[] getDaysOfFreeTime() {
        boolean arr[] = new boolean[6];
        for (int i = 0;
                i < dayStartEnd.length;
                i++) {
            if (dayStartEnd[i].startSession != null) {
                arr[i] = true;
            }
        }
        return arr;
    }
//  will get the number of days that has free time in

    public int getNumberOfDays() {
        int i = 0;
        for (int j = 0; j < 6; j++) {
            if (getDaysOfFreeTime()[j]) {
                i++;
            }
        }
        return i;
    }
//  will return an array of arraylist that has the free time in all days
//  as string

    public ArrayList[] getfreeTimeInDays() {
        ArrayList arr[] = new ArrayList[getNumberOfDays()];
        for (int i = 0, j = 0; i < 6; i++) {
            if (dayStartEnd[i].startSession != null) {

                arr[j] = new ArrayList();
                arr[j].add(i);
                arr[j].add(
                        dayStartEnd[i].value()
                );
                node n = dayStartEnd[i];
                while (n.next != null) {
                    n = n.next;
                    arr[j].add(
                            n.value()
                    );
                }
                j++;
            }
        }
        return arr;
    }
//  will return the day name for the day id

    public int getNumberOfFreeHours() {
        int x = 0;
        for (int i = 0;
                i < dayStartEnd.length;
                i++) {

            if (dayStartEnd[i].startSession != null) {
                node n = dayStartEnd[i];
                x += n.endSession - n.startSession;
                while (n.next != null) {
                    n = n.next;
                    x += n.endSession - n.startSession;
                }
            }
        }
        return x;
    }

    public int getNumberOfFreeHouresInDay(int day) {
        int x = 0;
        if (dayStartEnd[day].startSession != null) {
            node n = dayStartEnd[day];
            x += n.endSession - n.startSession;
            while (n.next != null) {
                n = n.next;
                x += n.endSession - n.startSession;
            }
        }
        return x;
    }

    public int[] getNumberOfFreeHouresInEachDay() {
        int x[] = new int[6];
        for (int i = 0;
                i < dayStartEnd.length;
                i++) {
            if (dayStartEnd[i].startSession != null) {
                node n = dayStartEnd[i];
                x[i] = n.endSession - n.startSession;
                while (n.next != null) {
                    n = n.next;
                    x[i] += n.endSession - n.startSession;
                }
            } else {
                x[i] = 0;
            }
        }
        return x;
    }

    public String getday(int day) {
        return switch (day) {
            case 0 ->
                "Saturday";
            case 1 ->
                "Sunday";
            case 2 ->
                "Monday";
            case 3 ->
                "Tuesday";
            case 4 ->
                "Wednesday";
            case 5 ->
                "Thursday";
            default ->
                "friday";
        };
    }
//  will add free time with anther object of free time

    public void add(FreeTime f) {
        for (int i = 0;
                i < 6;
                i++) {
            if (f.dayStartEnd[i].startSession != null) {
                addFreeTime(
                        i,
                        f.dayStartEnd[i].startSession,
                        f.dayStartEnd[i].endSession);
                node n = f.dayStartEnd[i];
                while (n.next != null) {
                    addFreeTime(
                            i,
                            n.next.startSession,
                            n.next.endSession);
                    n = n.next;
                }
            }
        }
    }
//  will format the free time array of arraylist grnrated with getfreeTimeInDays
//      as a readable string

    public String freeTimeG() {
        String s = "";
        for (int i = 0;
                i < getfreeTimeInDays().length;
                i++) {
            s += (getday(
                    Integer.parseInt(
                            getfreeTimeInDays()[i]
                                    .get(0) + "")
            )) + "\n";
            for (int j = 1;
                    j < getfreeTimeInDays()[i].size();
                    j++) {
                s += ("\t"
                        + getfreeTimeInDays()[i]
                                .get(j))
                        + "\n";
            }
        }
        return s;
    }
//  will remove an hour from the free time by getting the ID of the day 
//      and the hour as string (ex:11pm)

    public void removeAnHour(int day, String h) {
        int x;
        if (h.contains("am")) {
            x = Integer.parseInt(
                    h.substring(
                            0,
                            h.indexOf(
                                    "am")
                            - 1)
            ) + 12;

        } else {
            x = Integer.parseInt(
                    h.substring(
                            0,
                            h.indexOf("pm")
                    )
            );
        }

        node n = dayStartEnd[day];
        if (n.startSession == x) {
            n.startSession++;
        } else if (n.endSession - 1 == x) {
            n.endSession--;
        } else if (n.startSession < x
                && n.endSession > x) {
            int value = n.endSession;
            n.endSession = x;
            while (n.next != null) {
                n = n.next;
            }
            n.next = new node();
            n.next.startSession = x + 1;
            n.next.endSession = value;
        } else {
            while (n.next != null) {
                n = n.next;
                if (n.startSession == x) {
                    n.startSession++;
                    break;
                } else if (n.endSession - 1 == x) {
                    n.endSession--;
                    break;
                } else if (n.startSession < x
                        && n.endSession > x) {
                    int value = n.endSession;
                    n.endSession = x;
                    while (n.next != null) {
                        n = n.next;
                    }
                    n.next = new node();
                    n.next.startSession = x + 1;
                    n.next.endSession = value;
                    break;
                }
            }
        }

    }
//  will remove an hour from the free time by getting the ID of the day 
//      and the hour with its numbre (ex:23) from 0 to 23 0 is 12am 23 is 11 pm

    public void removeAnHour(int day,
            int hour) {
        node n = dayStartEnd[day];
        if (n.startSession == hour) {

            n.startSession++;

        } else if (n.endSession - 1 == hour) {

            n.endSession--;

        } else if (n.startSession < hour
                && n.endSession > hour) {

            int value = n.endSession;
            n.endSession = hour;
            while (n.next != null) {
                n = n.next;
            }
            n.next = new node();
            n.next.startSession = hour + 1;
            n.next.endSession = value;

        } else {
            while (n.next != null) {
                n = n.next;
                if (n.startSession == hour) {

                    n.startSession++;
                    break;

                } else if (n.endSession - 1 == hour) {

                    n.endSession--;
                    break;

                } else if (n.startSession < hour
                        && n.endSession > hour) {

                    int value = n.endSession;
                    n.endSession = hour;
                    while (n.next != null) {
                        n = n.next;
                    }
                    n.next = new node();
                    n.next.startSession = hour + 1;
                    n.next.endSession = value;
                    break;
                }
            }
        }

    }
//  return true or false if it is free at spacific time 

    public boolean isFreeAt(int day, int h) {

        node n = dayStartEnd[day];
        if (n.startSession == null || n.endSession == null) {
            return false;
        }
        if (h >= n.startSession && h < n.endSession) {
            return true;
        } else {
            while (n.next != null) {
                n = n.next;
                if (n.startSession == null || n.endSession == null) {
                    return false;
                }
                if (h >= n.startSession && h < n.endSession) {
                    return true;
                }
            }

        }
        return false;
    }
//  return the number of free hours in the week

    public int numberOfFreeHours() {
        int x = 0;
        if (dayStartEnd != null) {
            for (int i = 0; i < dayStartEnd.length; i++) {
                if (dayStartEnd[i] != null && dayStartEnd[i].endSession != null) {
                    x += dayStartEnd[i].endSession - dayStartEnd[i].startSession;
                    node n = dayStartEnd[i];
                    while (n.next != null) {
                        n = n.next;
                        if (dayStartEnd[i] != null) {
                            x += dayStartEnd[i].endSession - dayStartEnd[i].startSession;
                        }
                    }
                }
            }

        }

        return x;
    }

    public int[] getTheFirstFreeHour() {
        int x[] = new int[2];
        for (int i = 0; i < 6; i++) {
            if (dayStartEnd[i].startSession != null) {
                x[0] = i;
                x[1] = dayStartEnd[i].startSession;
                break;
            }
        }
        return x;
    }

    public void setDataForStuff(int staffId) {

    }

    public void getDataForStuff(int StaffId) {
        connection conn = new connection();
        try {
            ResultSet rs;
            rs = conn.select("select * from freetimeforstaff where StaffId =" + StaffId);
            while (rs.next()) {
                addFreeTime(rs.getInt(2), rs.getInt(3), rs.getInt(4));
            }
        } catch (Exception e) {
            System.out.println(5 + "" + e);
        } finally {
            conn.close();
        }
    }

    public void setDataForRooms(int RoomId) {

    }

    public void getDataForRooms(int RoomId) {
        connection conn = new connection();
        try {
            ResultSet rs;
            rs = conn.select("select * from freetimeforRooms where roomId =" + RoomId);
            while (rs.next()) {
                addFreeTime(rs.getInt(2), rs.getInt(3), rs.getInt(4));
            }
        } catch (Exception e) {
            System.out.println(5 + "" + e);
        } finally {
            conn.close();
        }
    }

    public static FreeTime AnddingTwoFreeTimes(FreeTime freeTime1, FreeTime freeTime2) {
        FreeTime f = new FreeTime();
        for (int i = 0; i < freeTime1.dayStartEnd.length; i++) {
            if (freeTime1.dayStartEnd[i].startSession != null && freeTime2.dayStartEnd[i].startSession != null) {
                node node1, node2;
                node1 = freeTime1.dayStartEnd[i];
                node2 = freeTime2.dayStartEnd[i];
                if (node1.startSession.equals(node2.startSession)) {
                    if (node1.endSession.equals(node2.endSession)) {
                        f.addFreeTime(i, node1.startSession, node1.endSession);
                        node1 = node1.next;
                        node2 = node2.next;
                    } else if (node1.endSession > node2.endSession) {
                        f.addFreeTime(i, node1.startSession, node2.endSession);
                        node1 = node1.next;
                    } else {
                        f.addFreeTime(i, node1.startSession, node1.endSession);
                        node1 = node1.next;
                    }
                } else if (node1.startSession > node2.startSession) {
                    if (node1.startSession > node2.endSession) {
                        node2 = node2.next;
                    } else if (node1.endSession > node2.endSession) {
                        f.addFreeTime(i, node1.startSession, node2.endSession);
                        node2 = node2.next;
                    } else {
                        f.addFreeTime(i, node1.startSession, node1.endSession);
                        node1 = node1.next;
                    }
                } else {
                    if (node1.startSession < node2.endSession) {
                        node1 = node1.next;
                    } else if (node1.endSession > node2.endSession) {
                        f.addFreeTime(i, node2.startSession, node2.endSession);
                        node2 = node2.next;
                    } else {
                        f.addFreeTime(i, node2.startSession, node1.endSession);
                        node1 = node1.next;
                    }
                }
                while (node1 != null && node2 != null) {
                    if (node1.startSession != null && node2.startSession != null) {
                        if (node1.startSession.equals(node2.startSession)) {
                            if (node1.endSession.equals(node2.endSession)) {
                                f.addFreeTime(i, node1.startSession, node1.endSession);
                                node1 = node1.next;
                                node2 = node2.next;
                            } else if (node1.endSession > node2.endSession) {
                                f.addFreeTime(i, node1.startSession, node2.endSession);
                                node1 = node1.next;
                            } else {
                                f.addFreeTime(i, node1.startSession, node1.endSession);
                                node1 = node1.next;
                            }
                        } else if (node1.startSession > node2.startSession) {
                            if (node1.startSession > node2.endSession) {
                                node2 = node2.next;
                            } else if (node1.endSession > node2.endSession) {
                                f.addFreeTime(i, node1.startSession, node2.endSession);
                                node2 = node2.next;
                            } else {
                                f.addFreeTime(i, node1.startSession, node1.endSession);
                                node1 = node1.next;
                            }
                        } else {
                            if (node1.startSession < node2.endSession) {
                                node1 = node1.next;
                            } else if (node1.endSession > node2.endSession) {
                                f.addFreeTime(i, node2.startSession, node2.endSession);
                                node2 = node2.next;
                            } else {
                                f.addFreeTime(i, node2.startSession, node1.endSession);
                                node1 = node1.next;
                            }
                        }
                    }
                }
            }
        }

        return f;

    }
}

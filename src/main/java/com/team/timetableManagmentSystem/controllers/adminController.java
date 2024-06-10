package com.team.timetableManagmentSystem.controllers;

import com.team.timetableManagmentSystem.DTOs.*;
import com.team.timetableManagmentSystem.service.InsertTimetable;
import com.team.timetableManagmentSystem.service.adminService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
@RequestMapping("/api/admin")
public class adminController {

    @Autowired
    private adminService adminService;

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    public ResponseEntity<?> adminResponse(HttpSession session) {
        ArrayList<String> s = new ArrayList<>();
        System.out.println(session.getAttribute("username"));
        if (session.getAttribute("username") != null) {
            if (session.getAttribute("role").equals(1)) {
                s.add("done");
                return new ResponseEntity<>(s, HttpStatus.OK);
            } else {
                s.add("NotAdmin");
                return new ResponseEntity<>(s, HttpStatus.OK);
            }
        } else {
            s.add("login first");
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("/addBranch")
    public Object addBranch(@RequestBody Branch branch, HttpSession session) {
        if (isadmin(session)) {
            if (!branch.getName().isEmpty() && !branch.getName().equals(null)) {
                if (!adminService.branchNameExist(branch.getName())) {
                    adminService.insertNewBranch(branch.getName());
                } else {
                    return new String[]{"branch Exist"};
                }

            } else {
                return new String[]{"empty name"};
            }
        }
        return adminResponse(session);

    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("/addBranches")
    public Object addBranches(HttpSession session, @RequestBody Branch... branchs) {
        if (isadmin(session)) {
            if (branchs.length > 0) {
                ArrayList<Branch> branchs1 = new ArrayList<>();
                for (int i = 0; i < branchs.length; i++) {
                    if (!branchs[i].getName().equals(null) && !branchs[i].getName().isEmpty() && !adminService.branchNameExist(branchs[i].getName())) {
                        branchs1.add(branchs[i]);
                    }
                }
                StringBuilder sb = new StringBuilder();
                if (branchs1.size() > 0) {
                    for (Branch branch : branchs) {
                        sb.append("('").append(branch.getName()).append("'),");
                    }
                } else {
                    return new String[]{"data either exist or empty"};
                }

                adminService.insertNewBranches(sb.toString().substring(0, sb.toString().length() - 1));
            } else {
                return new String[]{"empty branches"};
            }

        }
        return adminResponse(session);

    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllBranches")
    public Object getAllBranches(HttpSession session) {
        if (isadmin(session)) {
            ArrayList<Branch> branches = adminService.getAllBranches();
            return branches;
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editBranchName")
    public ResponseEntity<?> editBranchName(@RequestBody Branch branch, HttpSession session) {
        if (isadmin(session)) {
            adminService.editBranchName(branch.getId(), branch.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("removeBranch")
    public ResponseEntity<?> RemoveBranch(@RequestBody Branch branch, HttpSession session) {
        if (isadmin(session)) {
            adminService.removeBranch(branch.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @GetMapping("getAllRoomsInAllBranches")
    public Object GetallRoomsInAllBranches(HttpSession session) {
        if (isadmin(session)) {
            return adminService.GetallRoomsInAllBranches();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllRoomsInOneBranch")
    public Object GetallRoomsInOneBranch(HttpSession session, @RequestBody Branch branch) {
        if (isadmin(session)) {
            return adminService.GetallRoomsInoneBranch(branch.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("DeleteRoom")
    public ResponseEntity<?> DeleteRoom(HttpSession session, @RequestBody room room) {
        if (isadmin(session)) {
            adminService.removeRoom(room.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addRoom")
    public ResponseEntity<?> addRoom(HttpSession session, @RequestBody room room) {
        if (isadmin(session)) {
            adminService.addRoom(room.getName(), room.getCapacity(), room.getRoomtype().getId(), room.getBranch().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("updateRoom")
    public Object updateRoom(HttpSession session, @RequestBody room room) {
        if (isadmin(session)) {
            adminService.updateRoom(room.getId(), room.getName(), room.getCapacity(), room.getBranch().getId(), room.getRoomtype().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllRoomType")
    public Object getAllroomType(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getallRoomType();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addRooms")
    public ResponseEntity<?> addRooms(HttpSession session, @RequestBody room... rooms) {
        if (isadmin(session)) {
            StringBuilder sb = new StringBuilder();
            for (room room : rooms) {
                sb.append("( '").append(room.getName()).append("' ,").append(room.getCapacity()).append(",").append(room.getRoomtype().getId()).append(",").append(room.getBranch().getId()).append("),");
            }
            adminService.AddRooms(sb.toString().substring(0, sb.toString().length() - 1));
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addStaff")
    public ResponseEntity<?> addStaff(HttpSession session, @RequestBody Staff staff) {
        if (isadmin(session)) {
            adminService.addStaff(staff.getName(), staff.getType().getId(), staff.getBranch().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addStaffs")
    public ResponseEntity<?> addStaff(HttpSession session, @RequestBody Staff... staffs) {
        if (isadmin(session)) {
            StringBuilder sb = new StringBuilder();
            for (Staff staff : staffs) {
                sb.append("('").append(staff.getName()).append("',").append(staff.getType().getId()).append(",").append(staff.getBranch().getId()).append("),");
            }
            adminService.addStaffs(sb.toString().substring(0, sb.length() - 1));
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllStaff")
    public Object getAllStaffInAllBranches(HttpSession session) {
        if (isadmin(session)) {
            System.out.println(session.getAttribute("username"));
            return adminService.getAllStaffInAllBranches();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllUserRoles")
    public Object getAllUserRoles(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllRoles();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllStaffInOneBranch")
    public Object getAllStaffInOneBranch(HttpSession session, @RequestBody Branch branch) {
        if (isadmin(session)) {
            return adminService.getAllStaffInOneBranch(branch.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllStaffWithType")
    public Object getAllStaffWithType(HttpSession session, @RequestBody Staff staff) {
        if (isadmin(session)) {
            return adminService.getAllStaffInAllBranchesWithType(staff.getType().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllStaffWithTypeInOneBranch")
    public Object getAllStaffWithTypeInOneBranch(HttpSession session, @RequestBody Staff staff) {
        if (isadmin(session)) {
            return adminService.getAllStaffInAllBranchesWithTypeInOneBranch(staff.getBranch().getId(), staff.getType().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("updateStaff")
    public ResponseEntity<?> editStaffName(HttpSession session, @RequestBody Staff staff) {
        if (isadmin(session)) {
            if (staff.getId() == 0) {
                System.out.println("no id");
            }
            if (staff.getName() == null) {
                System.out.println("noname");
            }
            if (staff.getBranch() == null) {
                System.out.println("no branch");
            }
            if (staff.getType() == null) {
                System.out.println("notype");
            }

            System.out.println(staff.getId() + "\t" + staff.getName() + "\t" + staff.getBranch().getId() + "\t" + staff.getType().getId());
            adminService.updateStaff(staff.getId(), staff.getName(), staff.getBranch().getId(), staff.getType().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editStaffType")
    public ResponseEntity<?> editStaffType(HttpSession session, @RequestBody Staff staff) {
        if (isadmin(session)) {
            adminService.editStaffType(staff.getId(), staff.getType().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editStaffBranch")
    public ResponseEntity<?> editStaffBranch(HttpSession session, @RequestBody Staff staff) {
        if (isadmin(session)) {
            adminService.editStaffBranch(staff.getId(), staff.getBranch().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("removeStaff")
    public ResponseEntity<?> removeStaff(HttpSession session, @RequestBody Staff staff) {
        if (isadmin(session)) {
            adminService.removeStaff(staff.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addFaculty")
    public ResponseEntity<?> addFaculty(@RequestBody Faculty faculty, HttpSession session) {
        if (isadmin(session)) {
            adminService.addFaculty(faculty.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getFacultys")
    public Object getFacultys(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getFacultys();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("deleteFaculty")
    public ResponseEntity<?> deleteFaculty(@RequestBody Faculty faculty, HttpSession session) {
        if (isadmin(session)) {
            adminService.removeFaculty(faculty.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editFacultyName")
    public ResponseEntity<?> editFacultyName(HttpSession session, @RequestBody Faculty faculty) {
        if (isadmin(session)) {
            adminService.EditFacultyName(faculty.getId(), faculty.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addStudyPlan")
    public ResponseEntity<?> addStudyPlan(HttpSession session, @RequestBody StudyPlan studyPlan) {
        if (isadmin(session)) {
            adminService.insertStudyPlan(studyPlan.getName(), studyPlan.getFaculty().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editStudyPlanName")
    public ResponseEntity<?> editStudyPlanName(HttpSession session, @RequestBody StudyPlan studyPlan) {
        if (isadmin(session)) {
            adminService.editStudyPlanName(studyPlan.getId(), studyPlan.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editStudyPlan")
    public Object editStudyPlan(HttpSession session, @RequestBody StudyPlan studyPlan) {
        if (isadmin(session)) {
            adminService.UpdateStudyPlan(studyPlan.getId(), studyPlan.getFaculty().getId(), studyPlan.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editStudyPlanFaculty")
    public ResponseEntity<?> editStudyPlanFaculty(@RequestBody StudyPlan studyPlan, HttpSession session) {
        if (isadmin(session)) {
            adminService.editStudyPlanfaculty(studyPlan.getId(), studyPlan.getFaculty().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("removeStudyPlan")
    public ResponseEntity<?> removeStudyPlan(@RequestBody StudyPlan studyPlan, HttpSession session) {
        if (isadmin(session)) {
            adminService.removeStudyPlans(studyPlan.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllStudyPlans")
    public Object getAllStudyPlans(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllStudyPlans();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getStudyPlansInFaculty")
    public Object getStudyPlansInFaculty(HttpSession session, @RequestBody Faculty faculty) {
        if (isadmin(session)) {
            return adminService.getStudyPlansInFaculty(faculty.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addSemester")
    public ResponseEntity<?> addSemester(HttpSession session, @RequestBody Semester semester) {
        if (isadmin(session)) {
            adminService.addSemester(semester.getNumber(), semester.getStudyPlan().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editSemester")
    public Object editSemester(HttpSession session, @RequestBody Semester semester) {
        if (isadmin(session)) {
            adminService.UpdateSemester(semester.getId(), semester.getNumber(), semester.getStudyPlan().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editSemesterNumber")
    public ResponseEntity<?> editSemesterNumber(HttpSession session, @RequestBody Semester semester) {
        if (isadmin(session)) {
            adminService.editSemesterNumber(semester.getId(), semester.getNumber());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editSemesterStudyPlan")
    public ResponseEntity<?> editSemesterStudyPlan(HttpSession session, @RequestBody Semester semester) {
        if (isadmin(session)) {
            adminService.editSemesterStudyPlan(semester.getId(), semester.getStudyPlan().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("removeSemester")
    public ResponseEntity<?> removeSemester(HttpSession session, @RequestBody Semester semester) {
        if (isadmin(session)) {
            adminService.removeSemester(semester.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllSemesters")
    public Object getAllSemesters(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllSemesters();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getSemestersInStudyPlan")
    public Object getSemestersInStudyPlan(HttpSession session, @RequestBody StudyPlan studyPlan) {
        if (isadmin(session)) {
            return adminService.getAllSemestersInStudyPlan(studyPlan.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getSemestersInFaculty")
    public Object getSemestersInfaculty(HttpSession session, @RequestBody Faculty faculty) {
        if (isadmin(session)) {
            return adminService.getAllSemestersInFaculty(faculty.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addUser")
    public Object addUser(@RequestBody user user, HttpSession session) {
        if (isadmin(session)) {
            if (user.getUsername() != null && user.getPassword() != null && user.getRole() != 0) {
                adminService.addUser(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
            } else {
                System.out.println("username : " + user.getUsername() + "\npassword : " + user.getPassword() + "\nrole : " + user.getRole());
            }
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("changeUserRole")
    public Object changRole(@RequestBody user user, HttpSession session) {
        if (isadmin(session)) {
            if (user.getId() != 0 && user.getRole() != 0) {
                adminService.changeUserRole(user.getId(), user.getRole());
            }
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllUsers")
    public Object getAllUsers(HttpSession session) {
        if (isadmin(session)) {
            return adminService.GetUsers();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllStaffWithoutExistingUsers")
    public Object getAllStaffWithoutExistingUsers(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllStaffInAllBranchesWithOutExistingUsers();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editUserPassword")
    public Object editUserPassword(@RequestBody user user, HttpSession session) {
        if (isadmin(session)) {
            adminService.changeUserPassword(user.getId(), user.getPassword());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editUser")
    public Object editUser(@RequestBody user user, HttpSession session) {
        if (isadmin(session)) {
            adminService.editUser(user.getId(), user.getUsername(), user.getRole());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("updateUser")
    public Object updateUser(HttpSession session, @RequestBody user user) {
        if (isadmin(session)) {
            adminService.updateUser(user.getId(), user.getUsername(), user.getRole(), user.getPassword());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("deleteUser")
    public Object deleteUser(HttpSession session, @RequestBody user user) {
        if (isadmin(session)) {
            adminService.deleteUser(user.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("createLectuerTimetable")
    public Object createLectureTimetable(HttpSession session, @RequestBody TimeTableNameORIdAndBranch_WithCourses nameWithCourses) throws InterruptedException {
        if (isadmin(session)) {

            ArrayList<timeInTimetable> t = adminService.createLectureTimeTable(nameWithCourses.getName(), nameWithCourses.getCourses());
            InsertTimetable insertTimeTable = new InsertTimetable((timeInTimetable[]) t.toArray(new timeInTimetable[t.size()]));
            insertTimeTable.start();
            while (insertTimeTable.isAlive()) {
                Thread.sleep(100);
            }
            System.out.println((t.size() > 0) ? t.get(0).getTimetable().getId() : -1);
            return adminService.getATimetable((t.size() > 0) ? t.get(0).getTimetable().getId() : -1);
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("viewTimetable")
    public Object viewTimetable(HttpSession session, @RequestBody Timetable timetable) {
        if (isadmin(session)) {
            return adminService.getLectuerTimesInTimetable(timetable.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getFreeTimeForStaff")
    public Object getFreeTimeForStaff(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getFreeTimeForStaff();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addFreeTimeForStaff")
    public ResponseEntity<?> addFreeTimeForStaff(HttpSession session, freeTime freeTime) {
        if (isadmin(session)) {
            adminService.addFreeTimeForStaff(freeTime);
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addFreeTimeForRoom")
    public ResponseEntity<?> addFreeTimeForRoom(HttpSession session, @RequestBody room room, freeTime freeTime) {
        if (isadmin(session)) {
            adminService.addFreeTimeForRooms(room.getId(), freeTime);
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editFreeTimeForStaff")
    public ResponseEntity<?> editFreeTimeForStaff(HttpSession session, @RequestBody Staff staff, freeTime freeTime) {
        if (isadmin(session)) {
            adminService.updateFreeTimeForStaff(freeTime);
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editFreeTimeForRoom")
    public ResponseEntity<?> editFreeTimeForRoom(HttpSession session, @RequestBody room room, freeTime freeTime) {
        if (isadmin(session)) {
            adminService.updateFreeTimeForRooms(room.getId(), freeTime);
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addLectureGroup")
    public ResponseEntity<?> addLectureGroup(HttpSession session, @RequestBody LectureGroup lectuerGoup) {
        if (isadmin(session)) {
            adminService.addLectureGroup(lectuerGoup.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editLectuerGroupName")
    public ResponseEntity<?> editLectuerGroupName(HttpSession session, @RequestBody LectureGroup lectuerGroup) {
        if (isadmin(session)) {
            adminService.editLectuerGroupName(lectuerGroup.getId(), lectuerGroup.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("removeLectuerGroup")
    public ResponseEntity<?> removeLectuerGroup(HttpSession session, @RequestBody LectureGroup lectureGroup) {
        if (isadmin(session)) {
            adminService.removeLectuerGroup(lectureGroup.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllLectuerGroups")
    public Object getAllLectuerGroups(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllLectuerGroups();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addLecGroupInALectuerGroup")
    public ResponseEntity<?> addLecGroupInALectuerGroup(HttpSession session, @RequestBody LecGroup lecGroup) {
        if (isadmin(session)) {
            adminService.addLecGroupInALectuerGroup(lecGroup.getLectuerGoup().getId(), lecGroup.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editLecGroupInALectuerGroupName")
    public ResponseEntity<?> editLecGroupInALectuerGroupName(HttpSession session, @RequestBody LecGroup lecGroup) {
        if (isadmin(session)) {
            adminService.editLecGroupInALectuerGroupName(lecGroup.getId(), lecGroup.getName());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("editLecGroupInALectuerGroupLectuerGroup")
    public ResponseEntity<?> editLecGroupInALectuerGroupLectuerGroup(HttpSession session, @RequestBody LecGroup lecGroup) {
        if (isadmin(session)) {
            adminService.editLecGroupInALectuerGroupLectuerGroup(lecGroup.getId(), lecGroup.getLectuerGoup().getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("removeLecGroupInALectuerGroupLectuerGroup")
    public ResponseEntity<?> removeLecGroupInALectuerGroupLectuerGroup(HttpSession session, LecGroup lecGroup) {
        if (isadmin(session)) {
            adminService.removeLecGroupInALectuerGroupLectuerGroup(lecGroup.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("addBranchInALecGroup")
    public ResponseEntity<?> addBranchInALecGroup(HttpSession session, @RequestBody LecGroup lecGroup) {
        if (isadmin(session)) {
            adminService.addBranchInALecGroup(lecGroup.getBranchs().get(0).getId(), lecGroup.getId());
        }
        return adminResponse(session);

    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("removeBranchFromLecGroup")
    public ResponseEntity<?> removeBranchFromLecGroup(HttpSession session, @RequestBody LecGroup lecGroup) {
        if (isadmin(session)) {
            adminService.removeBranchFromLecGroup(lecGroup.getBranchs().get(0).getId(), lecGroup.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllBranchesInLecGroup")
    public Object getAllBranchesInLecGroup(HttpSession session, @RequestBody LecGroup lecGroup) {
        if (isadmin(session)) {
            return adminService.getAllBranchesInLecGroup(lecGroup.getId());
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllLecGroups")
    public Object getAllLecGroups(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllLecGroups();
        }
        return adminResponse(session);
    }

    @CrossOrigin(allowCredentials = "true", origins = "localhost:4200", originPatterns = "*")
    @RequestMapping("getAllLecGroupBranches")
    public Object getAllLecGroupBranches(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllLecGroupbranchs();
        }
        return adminResponse(session);
    }

    @RequestMapping("addSectionGroup")
    public ResponseEntity<?> addSectionGroup(HttpSession session, @RequestBody SectionGroup sectionGroup) {
        if (isadmin(session)) {
            adminService.addSectionGroup(sectionGroup.getName());
        }
        return adminResponse(session);
    }

    @RequestMapping("editSectionGroupName")
    public ResponseEntity<?> editSectionGroupName(HttpSession session, @RequestBody SectionGroup sectionGroup) {
        if (isadmin(session)) {
            adminService.editSectionGroupName(sectionGroup.getId(), sectionGroup.getName());
        }
        return adminResponse(session);
    }

    @RequestMapping("removeSectionGroup")
    public ResponseEntity<?> removeSectionGroup(HttpSession session, @RequestBody SectionGroup sectionGroup) {
        if (isadmin(session)) {
            adminService.removeSectionGroup(sectionGroup.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getAllSectionGroups")
    public Object getAllSectionGroups(HttpSession session) {
        if (isadmin(session)) {
            adminService.getAllSectionGroups();
        }
        return adminResponse(session);
    }

    @RequestMapping("insertBranchInSectionGroup")
    public ResponseEntity<?> insertBranchInSectionGroup(HttpSession session, @RequestBody sectionGroupBranchs sectionGroupBranchs) {
        if (isadmin(session)) {
            adminService.insertBranchInSectionGroup(sectionGroupBranchs.getBranchWithNumberOfSectionGroupses().get(0).getBranch().getId(), sectionGroupBranchs.getSectionGroup().getId(), sectionGroupBranchs.getBranchWithNumberOfSectionGroupses().get(0).getNumberOfSectionGroups());
        }
        return adminResponse(session);
    }

    @RequestMapping("editBranchSectionGroupnumberOfGroups")
    public ResponseEntity<?> editBranchSectionGroupnumberOfGroups(HttpSession session, @RequestBody sectionGroupBranchs sectionGroupBranchs) {
        if (isadmin(session)) {
            adminService.editBranchSectionGroupnumberOfGroups(sectionGroupBranchs.getBranchWithNumberOfSectionGroupses().get(0).getBranch().getId(), sectionGroupBranchs.getSectionGroup().getId(), sectionGroupBranchs.getBranchWithNumberOfSectionGroupses().get(0).getNumberOfSectionGroups());
        }
        return adminResponse(session);
    }

    @RequestMapping("changeBranchSectionGroup")
    public ResponseEntity<?> changeBranchSectionGroup(HttpSession session, @RequestBody sectionGroupBranchs sectionGroupBranchs, @RequestBody sectionGroupBranchs sectionGroupBranchs1) {

        if (isadmin(session)) {
            adminService.changeBranchSectionGroup(sectionGroupBranchs.getBranchWithNumberOfSectionGroupses().get(0).getBranch().getId(), sectionGroupBranchs.getSectionGroup().getId(), sectionGroupBranchs1.getSectionGroup().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("removeBranchFromSectionGroup")
    public ResponseEntity<?> removeBranchFromSectionGroup(HttpSession session, sectionGroupBranchs sectionGroupBranchs) {

        if (isadmin(session)) {
            adminService.removeBranchFromSectionGroup(sectionGroupBranchs.getBranchWithNumberOfSectionGroupses().get(0).getBranch().getId(), sectionGroupBranchs.getSectionGroup().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getSectionGroupBranchs")
    public Object getSectionGroupBranchs(HttpSession session, @RequestBody SectionGroup sectionGroup) {
        if (isadmin(session)) {
            return adminService.getSectionGroupBranchs(sectionGroup.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("insertCourse")
    public ResponseEntity<?> insertCourse(HttpSession session, @RequestBody course course) {
        if (isadmin(session)) {
            adminService.insertCourse(course.getName(), course.getCode(), course.getLectureHours(), course.getLabHours(), course.getSemester().getId(), course.getStudyPlan().getId(), course.getFaculty().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("setLecGroupIdForCourse")
    public ResponseEntity<?> setLecGroupIdForCourse(HttpSession session, @RequestBody course course) {
        if (isadmin(session)) {
            adminService.setLecGroupIdForCourse(course.getId(), course.getLectuerGoup().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("setSectionGroupIdForCourse")
    public ResponseEntity<?> setSectionGroupIdForCourse(HttpSession session, course course) {
        if (isadmin(session)) {
            adminService.setSectionGroupIdForCourse(course.getId(), course.getGroup().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getALLCourses")
    public Object getALLCourses(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getALLCourses();
        }
        return adminResponse(session);
    }

    @RequestMapping("getALLCoursesInSemester")
    public Object getALLCoursesInSemester(HttpSession session, @RequestBody Semester semester) {
        if (isadmin(session)) {
            return adminService.getALLCoursesInSemester(semester.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getALLCoursesInFaculty")
    public Object getALLCoursesInFaculty(HttpSession session, @RequestBody Faculty faculty) {
        if (isadmin(session)) {
            return adminService.getALLCoursesInFaculty(faculty.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getALLCoursesInStudyPlan")
    public Object getALLCoursesInStudyPlan(HttpSession session, @RequestBody StudyPlan studyPlan) {
        if (isadmin(session)) {
            return adminService.getALLCoursesInStudyPlan(studyPlan.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("insertCourseStaff")
    public ResponseEntity<?> insertCourseStaff(HttpSession session, @RequestBody CourseStaff courseStaff) {
        if (isadmin(session)) {
            adminService.insertCourseStaff(courseStaff.getCourse().getId(), courseStaff.getStaff().getId(), courseStaff.getBranch().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("deleteCourseStaff")
    public ResponseEntity<?> deleteCourseStaff(HttpSession session, @RequestBody CourseStaff courseStaff) {
        if (isadmin(session)) {
            adminService.deleteCourseStaff(courseStaff.getCourse().getId(), courseStaff.getStaff().getId(), courseStaff.getBranch().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("editCourseForCourseStaffInABranch")
    public ResponseEntity<?> editCourseForCourseStaffInABranch(HttpSession session, @RequestBody CourseStaff courseStaff) {
        if (isadmin(session)) {
            adminService.editCourseForCourseStaffInABranch(courseStaff.getCourse().getId(), courseStaff.getStaff().getId(), courseStaff.getBranch().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("editBranchForCourseStaffInCourse")
    public ResponseEntity<?> editBranchForCourseStaffInCourse(HttpSession session, @RequestBody CourseStaff courseStaff) {
        if (isadmin(session)) {
            adminService.editBranchForCourseStaffInCourse(courseStaff.getCourse().getId(), courseStaff.getStaff().getId(), courseStaff.getBranch().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("editCourseStaffInCourseForABranch")
    public ResponseEntity<?> editCourseStaffInCourseForABranch(HttpSession session, @RequestBody CourseStaff courseStaff) {
        if (isadmin(session)) {
            adminService.editCourseStaffInCourseForABranch(courseStaff.getCourse().getId(), courseStaff.getStaff().getId(), courseStaff.getBranch().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getAllCourseStaff")
    public Object getAllCourseStaff(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllCourseStaff();
        }
        return adminResponse(session);
    }

    @RequestMapping("getAllCourseStaffForACourse")
    public Object getAllCourseStaffForACourse(HttpSession session, @RequestBody course course) {
        if (isadmin(session)) {
            return adminService.getAllCourseStaffForACourse(course.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getAllCourseStaffInSemester")
    public Object getAllCourseStaffInSemester(HttpSession session, @RequestBody Semester semester) {
        if (isadmin(session)) {
            return adminService.getAllCourseStaffInSemester(semester.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getAllCourseStaffInStudyPlan")
    public Object getAllCourseStaffInStudyPlan(HttpSession session, StudyPlan studyPlan) {
        if (isadmin(session)) {
            return adminService.getAllCourseStaffInStudyPlan(studyPlan.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getAllCourseStaffInBranch")
    public Object getAllCourseStaffInBranch(HttpSession session, @RequestBody Branch branch) {
        if (isadmin(session)) {
            return adminService.getAllCourseStaffInBranch(branch.getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("getAllStaffTypeInCourseStaffInBranch")
    public Object getAllStaffTypeInCourseStaffInBranch(HttpSession session, @RequestBody Staff staff) {
        if (isadmin(session)) {
            return adminService.getAllStaffTypeInCourseStaffInBranch(staff.getBranch().getId(), staff.getType().getId());
        }
        return adminResponse(session);
    }

    @RequestMapping("createSectionTimetable")
    public Object createSectionTimetable(HttpSession session, @RequestBody TimeTableNameORIdAndBranch_WithCourses idAndBranch_WithCourses) {

//        if (isadmin(session)) {
        ArrayList<timeInTimetable> t = adminService.createSectionTimeTable(idAndBranch_WithCourses.getId(), idAndBranch_WithCourses.getBranchId(), idAndBranch_WithCourses.getCourses());
        InsertTimetable insertTimeTable = new InsertTimetable((timeInTimetable[]) t.toArray(new timeInTimetable[t.size()]));
        insertTimeTable.start();
        return t;
        //  }
        //  return adminResponse(session);
    }

    @RequestMapping("getJobTypes")
    public Object getJobTypes(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getAllJobTypes();
        }
        return adminResponse(session);
    }

    @RequestMapping("getTimeTable")
    public ArrayList<timeInTimetable> getTimetable(HttpSession session, Timetable timetable) {
        return adminService.getATimetable(timetable.getId());
    }

    @RequestMapping("getFreetimeForRooms")
    public Object getFreeTimeForRooms(HttpSession session) {
        if (isadmin(session)) {
            return adminService.getFreeTimeForRooms();
        }
        return adminResponse(session);
    }

    boolean isadmin(HttpSession session) {
        return session.getAttribute("role") != null
                && session.getAttribute("role").equals(1);
    }

}

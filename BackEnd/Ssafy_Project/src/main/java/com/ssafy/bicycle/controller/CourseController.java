package com.ssafy.bicycle.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.bicycle.model.dto.Course;
import com.ssafy.bicycle.model.dto.CourseMap;
import com.ssafy.bicycle.model.dto.Image;
import com.ssafy.bicycle.model.dto.SearchCondition;
import com.ssafy.bicycle.model.service.CourseMapService;
import com.ssafy.bicycle.model.service.CourseService;
import com.ssafy.bicycle.model.service.ImageService;

@RestController
@RequestMapping("/api")
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private CourseMapService courseMapService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ImageService imageService;

	private int courseNum = 15;

//	 등록
//	 지도에 체크를 해서 정보를 입력하고 넣겠지??
//	 courseMap이랑 course 필요하네
//	 경도 위도 값이 같이 들어올거임, 그리고 제목 타이들 등등이 들어올거임
	@PostMapping("/course")
	public ResponseEntity<?> writeCourse(@ModelAttribute Course course,
			@RequestParam(required = false) MultipartFile file) {
		try {
			System.out.println(course);
			System.out.println(file);
			if (file != null && file.getSize() > 0) {
				Resource res = resourceLoader.getResource("classpath:/static/upload"); // 경로
				Image image = new Image();
				image.setImage_type(1);
				image.setImage_boardNum(course.getCourse_num());
				image.setImage_oriName(file.getOriginalFilename());
				image.setImage_saveName(System.currentTimeMillis() + "_" + file.getOriginalFilename());
				image.setImage_boardNum(courseNum);
				file.transferTo(new File(res.getFile().getCanonicalFile() + "/" + image.getImage_saveName()));

				imageService.writeImage(image);

			}
			
			int result1 = courseService.writeCourse(course);
			boolean result2 = true;
			for (int seq = 0; seq < course.getCourseMap().size() / 2; seq++) {
				CourseMap cm = new CourseMap(courseNum, seq + 1, course.getCourseMap().get(seq * 2),
						course.getCourseMap().get(seq * 2 + 1));
				if (courseMapService.writeCourseMap(cm) == 0) {
					result2 = false;
				}
			}
			courseNum++;
			if (result1 == 1 && result2) {
				return new ResponseEntity<Course>(course, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 조회 or 조회
	@GetMapping("/course")
	public ResponseEntity<?> list(SearchCondition condition) {
//		System.out.println(condition);
		List<Course> list = courseService.search(condition);
		for (int i = 0; i < list.size(); i++) {
			int courseNum = list.get(i).getCourse_num();

			List<CourseMap> cmlist = courseMapService.getCourseMapList(courseNum);

			List<Double> courseMap = new ArrayList<Double>();

//			if (list == null || list.size() == 0) {
//				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
//			}

			for (int seq = 0; seq < cmlist.size(); seq++) {
				courseMap.add(cmlist.get(seq).getCm_lat());
				courseMap.add(cmlist.get(seq).getCm_lng());
			}
			
			HashMap<String,Integer> map = new HashMap<>();
			map.put("type", 1);
			map.put("num",courseNum);
			
			List<Image> imglist = imageService.getImageList(map);
			list.get(i).setCourse_imgName(imglist.get(0).getImage_saveName());

			list.get(i).setCourseMap(courseMap);
		}


		return new ResponseEntity<List<Course>>(list, HttpStatus.OK);
	}
	
	// 키워드 조회
	@GetMapping("/course/keyword/{senddata}")
	public ResponseEntity<?> list(@PathVariable String senddata){
		System.out.println("keyword - " + senddata);
		List<String> stringList = Arrays.asList(senddata.split("_"));
		List<Course> list = courseService.geKeywordList(stringList);
		
//		if (list == null || list.size() == 0) {
//			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
//		}
		
		for(int i = 0;i<list.size();i++) {
			HashMap<String,Integer> map = new HashMap<>();
			map.put("type", 1);
			map.put("num",list.get(i).getCourse_num());
			
			List<Image> imglist = imageService.getImageList(map);
			list.get(i).setCourse_imgName(imglist.get(0).getImage_saveName());
			
		}
		return new ResponseEntity<List<Course>>(list, HttpStatus.OK);
	}
	
	
//	
//	//다시

	// 상세조회
	@GetMapping("/course/{num}")
	public ResponseEntity<?> detail(@PathVariable int num){
		System.out.println(num);
		Course course = courseService.getCourseOne(num);
		
		if(course == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		HashMap<String,Integer> map = new HashMap<>();
		map.put("type", 1);
		map.put("num",num);
		
		List<Image> list = imageService.getImageList(map);
		course.setCourse_imgName(list.get(0).getImage_saveName());
		
		return new ResponseEntity<Course>(course,HttpStatus.OK);
	}
//	
//	// 수정
//	@PutMapping("/course")
//	public ResponseEntity<?> update(@RequestBody Course course){
//		if(courseService.modifyCourse(course)) {
//			return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
//		}
//		return new ResponseEntity<String>("FAIL", HttpStatus.BAD_REQUEST);
//	}
//	
//	
//	// 삭제
//	@DeleteMapping("/course/{num}")
//	public ResponseEntity<?> delete(@RequestBody int num) {
//		if (courseService.removeCourse(num)) {
//			return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
//		}
//		return new ResponseEntity<String>("FAIL", HttpStatus.BAD_REQUEST);
//	}
//	
}

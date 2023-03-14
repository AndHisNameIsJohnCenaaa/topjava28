package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController {


    private final MealService service;

    public JspMealController(MealService service) {
        this.service = service;
    }

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
        return "meals";
    }

    @GetMapping("/filter")
    public String getBetween(Model model,
                           @Nullable @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                           @Nullable @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                           @Nullable @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                           @Nullable @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        model.addAttribute("meals",
                MealsUtil.getFilteredTos(service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId()),
                        MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime));
        return "meals";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("meal") Meal meal) {
        return "mealForm";
    }

    @GetMapping("/{id}/update")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("meal", service.get(id, SecurityUtil.authUserId()));
        return "mealForm";
    }

    @PostMapping("/save")
    public String create(HttpServletRequest request) throws IOException {
//        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                null,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        service.create(meal, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @PostMapping("/{id}/save")
    public String update(HttpServletRequest request, @PathVariable("id") int id) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                id,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        service.update(meal, SecurityUtil.authUserId());
        return "redirect:/meals";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("meal", service.get(id, SecurityUtil.authUserId()));
        return "mealForm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }
}

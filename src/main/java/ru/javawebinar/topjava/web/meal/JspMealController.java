package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;

@Controller
@RequestMapping("/meals")
public class JspMealController {


    private final MealService service;

    public JspMealController(MealService service) {
        this.service = service;
    }

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @GetMapping()
    public String getMeals(Model model) {
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
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
    public String save(@ModelAttribute("meal") Meal meal) {
        if (meal.isNew()) {
            service.create(meal, SecurityUtil.authUserId());
            return "redirect:/meals";
        }
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

package cs509.backend.Controller;

import cs509.backend.Data.FlightForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormController {

    // IGNORE - just for testing with basic front end form
    @GetMapping("/showForm")
    public String showForm(Model model) {
        model.addAttribute("flightForm", new FlightForm());
        return "frontend";
    }
}

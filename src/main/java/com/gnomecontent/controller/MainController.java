package com.gnomecontent.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gnomecontent.dao.AppUserDAO;
import com.gnomecontent.documents.PubmedArticles;
import com.gnomecontent.entity.AppRole;
import com.gnomecontent.entity.AppUser;
import com.gnomecontent.entity.ContentSearchForm;
import com.gnomecontent.entity.Pubmed;
import com.gnomecontent.form.AppUserForm;
import com.gnomecontent.service.PubmedService;
import com.gnomecontent.utils.SecurityUtil;
import com.gnomecontent.utils.WebUtils;
import com.gnomecontent.validator.AppUserValidator;

@Controller
@Transactional
public class MainController {

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	private UsersConnectionRepository connectionRepository;

	@Autowired
	private AppUserValidator appUserValidator;

	@Autowired
	private PubmedService pubService;
	
	public static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("message", "Home page!");
		return "welcomePage";
	}

	
	/*
	 * @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	 * public String logoutSuccessfulPage(Model model) { model.addAttribute("title",
	 * "Logout"); System.out.println("logout called"); return
	 * "logoutSuccessfulPage"; }
	 */

/*	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();

			String userInfo = WebUtils.toString(loginedUser);

			model.addAttribute("userInfo", userInfo);

			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);

		}

		return "403Page";
	}*/

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		return "loginPage";
	}

	private AppUserForm getConnetionForm(WebRequest request) {

		ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator,
				connectionRepository);
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

		AppUserForm myForm = null;
		if (connection != null) {
			myForm = new AppUserForm(connection);

		} else {
			myForm = new AppUserForm();
		}

		return myForm;

	}

	@RequestMapping(value = { "/userLogin" }, method = RequestMethod.GET)
	public String signupPage(WebRequest request, Model model,@RequestParam(defaultValue = "1") int page) {

		AppUserForm myForm = getConnetionForm(request);
		/*Page<Pubmed> pubPage = pubService.getPubmedArticles(page);
		List<Pubmed> pubmedArticles = new ArrayList<Pubmed>();		
		pubPage.forEach(data -> pubmedArticles.add(data));*/
		model.addAttribute("myForm", myForm);
		model.addAttribute("searchForm", new ContentSearchForm());
		/*model.addAttribute("pubmedArticles", pubmedArticles);

		model.addAttribute("currentPage", page);
		model.addAttribute("totalSearchElementsCount", pubPage.getTotalElements());
		model.addAttribute("totalPages", pubPage.getTotalPages());
		model.addAttribute("totalArticles",pubPage.getTotalElements());
		model.addAttribute("size",pubPage.getSize());
		*/
		return "pubmedArticlesList";
		
	}


		
	@RequestMapping(value = "/searchByPmid")
	public String getArticlesByPmid(@ModelAttribute("searchForm") ContentSearchForm searchForm, Model model,
			WebRequest request,@RequestParam(defaultValue = "1") int page) {
		Page<Pubmed> pubPage = pubService.searchArticlesByPmid(searchForm.getSearchTerm(),page);
		AppUserForm myForm = getConnetionForm(request);
		List<Pubmed> pubmedArticles = new ArrayList<Pubmed>();		
		pubPage.forEach(data -> pubmedArticles.add(data));
		model.addAttribute("myForm", myForm);
		model.addAttribute("searchForm", new ContentSearchForm());
		model.addAttribute("pubmedArticles", pubmedArticles);
		model.addAttribute("searchKeyword", searchForm.getSearchTerm());
		//model.addAttribute("currentPage", page);
		model.addAttribute("totalSearchElementsCount", pubPage.getTotalElements());
		model.addAttribute("totalPages", pubPage.getTotalPages());
		model.addAttribute("totalArticles",pubPage.getTotalElements());
		System.out.println("totalPages..."+pubPage.getTotalPages());
		return "pubmedArticlesList";

	}

	@RequestMapping(value = "/getArticles")
	public String getAllArticles(@ModelAttribute("searchForm") ContentSearchForm searchForm, Model model,
			WebRequest request, @RequestParam("sword") String sword, @RequestParam("page") int page) {

		System.out.println("----------------------page:"+page);
		System.out.println("----------------------sword:"+sword);
		AppUserForm myForm = getConnetionForm(request);
		Page<Pubmed> pubPage=null;
		if(!sword.isEmpty() && sword!=null)
		 pubPage = pubService.searchArticlesByPmid(sword,page);
		else
		 pubPage = pubService.getPubmedArticles(page);
		
		List<Pubmed> pubmedArticles = new ArrayList<Pubmed>();		
		pubPage.forEach(data -> pubmedArticles.add(data));
		model.addAttribute("myForm", myForm);
		model.addAttribute("searchForm", new ContentSearchForm());
		model.addAttribute("pubmedArticles", pubmedArticles);
		model.addAttribute("searchKeyword", sword);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalSearchElementsCount", pubPage.getTotalElements());
		model.addAttribute("totalPages", pubPage.getTotalPages());
		model.addAttribute("totalArticles",pubPage.getTotalElements());
		model.addAttribute("size",pubPage.getSize());
		System.out.println("Total Elements..."+pubPage.getTotalElements());
		System.out.println("TotalPages..."+pubPage.getTotalPages());
		System.out.println("Number..."+pubPage.getNumber());
		System.out.println("Size..."+pubPage.getSize());
		
		return "pubmedArticlesList";
		

	}

/*	@RequestMapping("/getArticlesList")
	public String list(@ModelAttribute("searchForm") ContentSearchForm searchForm,Model model, Pageable pageable,WebRequest request){
		AppUserForm myForm = getConnetionForm(request);
		Page<Pubmed> aList = pubService.find(pageable);
		model.addAttribute("myForm", myForm);
		model.addAttribute("searchForm", new ContentSearchForm());
		model.addAttribute("page",aList);
		
		return "pubmedArticlesList";
	}
*/
	@RequestMapping(value="/getArticlesList")
	public String list(@ModelAttribute("searchForm") ContentSearchForm searchForm,Model model, Pageable pageable,WebRequest request){
		AppUserForm myForm = getConnetionForm(request);
		Page<PubmedArticles> aList = pubService.find(pageable);
		model.addAttribute("myForm", myForm);
		model.addAttribute("searchForm", new ContentSearchForm());
		model.addAttribute("page",aList);
		
		return "pubmedArticlesList";
	}
	@RequestMapping(value="/searchArticles",method = RequestMethod.POST)
	public String getArticles(@ModelAttribute("searchForm") ContentSearchForm searchForm,Model model, Pageable pageable,WebRequest request){
		AppUserForm myForm = getConnetionForm(request);
		Page<PubmedArticles> aList = pubService.getArticlesBySearch(searchForm.getSearchTerm(),pageable);
		model.addAttribute("myForm", myForm);
		model.addAttribute("searchForm", new ContentSearchForm());
		model.addAttribute("page",aList);
		
		return "pubmedArticlesList";
	}
	@RequestMapping(value="/getCardiologyArticles")
	public String getCardiologyList(@ModelAttribute("searchForm") ContentSearchForm searchForm,Model model, Pageable pageable,WebRequest request){
		AppUserForm myForm = getConnetionForm(request);
		Page<Pubmed> aList = pubService.getCardiologyArticles(pageable);
		model.addAttribute("myForm", myForm);
		model.addAttribute("searchForm", new ContentSearchForm());
		model.addAttribute("page",aList);
		
		return "cardiologyArticlesList";
	}
}

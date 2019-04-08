package softwire.training.myface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import softwire.training.myface.models.dbmodels.Post;
import softwire.training.myface.models.viewmodels.WallViewModel;
import softwire.training.myface.services.PostsService;
import softwire.training.myface.services.UsersService;

import java.security.Principal;

@Controller
@RequestMapping("/wall")
public class WallController {

    private final PostsService postsService;
    private final UsersService usersService;

    @Autowired
    public WallController(PostsService postsService, UsersService usersService) {
        this.postsService = postsService;
        this.usersService = usersService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView getWall(
            Principal principal
    ) {

        WallViewModel wallViewModel = new WallViewModel();
        wallViewModel.loggedInUsername = principal.getName();
        wallViewModel.wallOwnerUsername = principal.getName();
        wallViewModel.posts = postsService.getAllPosts();
        wallViewModel.wallOwnerFullName = usersService.getUserWithUserName(principal.getName()).get().getFullName();
        wallViewModel.allUsernames = usersService.guessAllUsernames();

        return new ModelAndView("Newsfeed", "model", wallViewModel);
    }

    @RequestMapping(value = "/{wallOwnerUsername}", method = RequestMethod.GET)
    public ModelAndView getWall(
            @PathVariable("wallOwnerUsername") String wallOwnerUsername,
            Principal principal
    ) {

        WallViewModel wallViewModel = new WallViewModel();
        wallViewModel.loggedInUsername = principal.getName();
        wallViewModel.wallOwnerUsername = wallOwnerUsername;
        wallViewModel.posts = postsService.getPostsOnWall(wallOwnerUsername);
        wallViewModel.wallOwnerFullName = usersService.getUserWithUserName(wallOwnerUsername).get().getFullName();
        wallViewModel.allUsernames = usersService.guessAllUsernames();

        return new ModelAndView("wall", "model", wallViewModel);
    }

    @RequestMapping(value = "/{wallOwnerUsername}/post", method = RequestMethod.POST)
    public RedirectView postOnWall(
            @PathVariable("wallOwnerUsername") String wallOwnerUsername,
            @ModelAttribute("content") String content,
            Principal loggedInUserPrincipal
    ) {

        String senderUsername = loggedInUserPrincipal.getName();
        postsService.createPost(senderUsername, wallOwnerUsername, content);

        return new RedirectView("/wall/" + wallOwnerUsername);
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public RedirectView updateStatus(
            @ModelAttribute("wallOwnerUsername") String wallOwnerUsername,
            @ModelAttribute("content") String content,
            Principal loggedInUserPrincipal
    ) {
        String senderUsername = loggedInUserPrincipal.getName();
        postsService.createPost(senderUsername, wallOwnerUsername, content);

        return new RedirectView("/wall/");
    }

    @RequestMapping(value = "/{wallOwnerUsername}/delete-post/{id}", method = RequestMethod.POST)
    public RedirectView deletePostOnWall(
            @PathVariable("wallOwnerUsername") String wallOwnerUsername,
            @PathVariable("id") Integer id,
            Principal principal
    ) {
        Post currentPost = postsService.getSinglePostOnWall(id);
        String sender = currentPost.getSender();
        String recipient = currentPost.getRecipient();
        String currentUser = principal.getName();

        if (currentUser.equals(sender) || currentUser.equals(recipient)) {
            postsService.deletePost(id);
            return new RedirectView("/wall/" + wallOwnerUsername);
        } else {
            throw new AccessDeniedException("Only the sender or recipient can delete posts!");
        }
    }

    @RequestMapping(value = "/delete-post/{id}", method = RequestMethod.POST)
    public RedirectView deletePostOnTimeline(
            @PathVariable("id") Integer id,
            Principal principal
    ) {
        Post currentPost = postsService.getSinglePostOnWall(id);
        String sender = currentPost.getSender();
        String recipient = currentPost.getRecipient();
        String currentUser = principal.getName();

        if (currentUser.equals(sender) || currentUser.equals(recipient)) {
            postsService.deletePost(id);
            return new RedirectView("/wall/");
        } else {
            throw new AccessDeniedException("Only the sender or recipient can delete posts!");
        }
    }

    @RequestMapping(value = "/{wallOwnerUsername}/wave", method = RequestMethod.POST)
    public RedirectView waveAtUser(
            @PathVariable("wallOwnerUsername") String wallOwnerUsername,
            Principal principal
    ) {
        String senderUsername = principal.getName();
        postsService.createPost(senderUsername, wallOwnerUsername, "\uD83D\uDC4B");
        return new RedirectView("/wall/" + wallOwnerUsername);
    }

    @RequestMapping(value = "/{wallOwnerUsername}/frown", method = RequestMethod.POST)
    public RedirectView frownAtUser(
            @PathVariable("wallOwnerUsername") String wallOwnerUsername,
            Principal principal
    ) {
        String senderUsername = principal.getName();
        postsService.createPost(senderUsername, wallOwnerUsername, "☹️");
        return new RedirectView("/wall/" + wallOwnerUsername);

    }@RequestMapping(value = "/{wallOwnerUsername}/like", method = RequestMethod.POST)
    public RedirectView sendLikeToUser(
            @PathVariable("wallOwnerUsername") String wallOwnerUsername,
            Principal principal
    ) {
        String senderUsername = principal.getName();
        postsService.createPost(senderUsername, wallOwnerUsername, "\uD83D\uDC4D");
        return new RedirectView("/wall/" + wallOwnerUsername);
    }


}
package softwire.training.myface.models.viewmodels;

import softwire.training.myface.models.dbmodels.Post;

import java.util.List;

public class WallViewModel {

    public String loggedInUsername;
    public String wallOwnerUsername;
    public String wallOwnerFullName;
    public List<Post> posts;
    public List<String> allUsernames;

}

package astrogeist.engine.persitence.userdatadefinitions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class UserDataDefinitions {
    final List<UserDataDefinition> userDataDefinitions = new ArrayList<>();

    public List<UserDataDefinition> getUserDataDefinitions() { return userDataDefinitions; }
    
    public List<String> getUserDataNames(){
    	var names = this.userDataDefinitions.stream()
    		.map(UserDataDefinition::name)
    		.collect(Collectors.toList());
    	return names;
    }
  
}

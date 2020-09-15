package scorpio1331.github.io.DaytimeVote;

public enum Permissions
{
    CanDebug("daytimevote.canDebug"),
    CanChangeConfig("daytimevote.canChangeConfig"),
    CanVote("daytimevote.canVote");

    private final String permissionName;

    Permissions(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }
}

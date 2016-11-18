package com.gordon.rrod.Sample.data;

import java.util.List;

/**
 * Created by Steven on 16/3/28.
 */
public class LoginInfoEntity {

    /**
     * Id : 0647154d-c87c-4b91-9553-7e7025e24545
     * TenantId : 2e44d71b-feb2-4ab0-a594-f7b4ba75eb36
     * Name : 业务管理员
     * Gender : male
     * LoginName : jzdev
     * CreateTime : 2015/05/28 14:05:14
     * LastLoginTime : 2016/02/01 16:17:07
     * Enable : true
     * Email : 64014803@qq.com
     * CurrentPositionId : 56d16567-2b0f-48a3-ba17-bbd902df1ad8
     * LoginTimes : 3560
     */

    private UserEntity user;
    /**
     * Id : 2e44d71b-feb2-4ab0-a594-f7b4ba75eb36
     * Name : jzdev
     * DisplayName : 九章平台技术部测试模板
     * Enable : true
     * AdminMail : null
     * Admin : d63f3678-5082-4c8a-9bd5-5aef58c2be08
     * IsRun : true
     * IsTemplate : true
     * CreateTime : 2015/05/28 00:00:00
     * EndTime : 2016/05/28 00:00:00
     * GISProvider : null
     */

    private TenantEntity tenant;
    /**
     * PositionId : 56d16567-2b0f-48a3-ba17-bbd902df1ad8
     * PositionName : 总经理
     * OUId : 9d308b1e-aa27-4968-8efc-0b265a1a6e73
     * OUName : 总经理办公室
     * RankId : 968a4c7f-1d4a-49f0-8ea0-dded8d3737c6
     * RankName : 总经理级
     * PositionDuty : null
     * UserPositions : [{"PositionId":"56d16567-2b0f-48a3-ba17-bbd902df1ad8","PositionName":"总经理办公室-总经理","PositionDuty":null}]
     * AccountSets : []
     * CurrentAccountSetName : null
     */

    private PositionInfoEntity positionInfo;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public void setTenant(TenantEntity tenant) {
        this.tenant = tenant;
    }

    public PositionInfoEntity getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(PositionInfoEntity positionInfo) {
        this.positionInfo = positionInfo;
    }

    public static class UserEntity {
        private String Id;
        private String TenantId;
        private String Name;
        private String Gender;
        private String LoginName;
        private String CreateTime;
        private String LastLoginTime;
        private boolean Enable;
        private String Email;
        private String CurrentPositionId;
        private int LoginTimes;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getTenantId() {
            return TenantId;
        }

        public void setTenantId(String TenantId) {
            this.TenantId = TenantId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getLoginName() {
            return LoginName;
        }

        public void setLoginName(String LoginName) {
            this.LoginName = LoginName;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getLastLoginTime() {
            return LastLoginTime;
        }

        public void setLastLoginTime(String LastLoginTime) {
            this.LastLoginTime = LastLoginTime;
        }

        public boolean isEnable() {
            return Enable;
        }

        public void setEnable(boolean Enable) {
            this.Enable = Enable;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getCurrentPositionId() {
            return CurrentPositionId;
        }

        public void setCurrentPositionId(String CurrentPositionId) {
            this.CurrentPositionId = CurrentPositionId;
        }

        public int getLoginTimes() {
            return LoginTimes;
        }

        public void setLoginTimes(int LoginTimes) {
            this.LoginTimes = LoginTimes;
        }
    }

    public static class TenantEntity {
        private String Id;
        private String Name;
        private String DisplayName;
        private boolean Enable;
        private Object AdminMail;
        private String Admin;
        private boolean IsRun;
        private boolean IsTemplate;
        private String CreateTime;
        private String EndTime;
        private Object GISProvider;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getDisplayName() {
            return DisplayName;
        }

        public void setDisplayName(String DisplayName) {
            this.DisplayName = DisplayName;
        }

        public boolean isEnable() {
            return Enable;
        }

        public void setEnable(boolean Enable) {
            this.Enable = Enable;
        }

        public Object getAdminMail() {
            return AdminMail;
        }

        public void setAdminMail(Object AdminMail) {
            this.AdminMail = AdminMail;
        }

        public String getAdmin() {
            return Admin;
        }

        public void setAdmin(String Admin) {
            this.Admin = Admin;
        }

        public boolean isIsRun() {
            return IsRun;
        }

        public void setIsRun(boolean IsRun) {
            this.IsRun = IsRun;
        }

        public boolean isIsTemplate() {
            return IsTemplate;
        }

        public void setIsTemplate(boolean IsTemplate) {
            this.IsTemplate = IsTemplate;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public Object getGISProvider() {
            return GISProvider;
        }

        public void setGISProvider(Object GISProvider) {
            this.GISProvider = GISProvider;
        }
    }

    public static class PositionInfoEntity {
        private String PositionId;
        private String PositionName;
        private String OUId;
        private String OUName;
        private String RankId;
        private String RankName;
        private Object PositionDuty;
        private Object CurrentAccountSetName;
        /**
         * PositionId : 56d16567-2b0f-48a3-ba17-bbd902df1ad8
         * PositionName : 总经理办公室-总经理
         * PositionDuty : null
         */

        private List<UserPositionsEntity> UserPositions;
        private List<?> AccountSets;

        public String getPositionId() {
            return PositionId;
        }

        public void setPositionId(String PositionId) {
            this.PositionId = PositionId;
        }

        public String getPositionName() {
            return PositionName;
        }

        public void setPositionName(String PositionName) {
            this.PositionName = PositionName;
        }

        public String getOUId() {
            return OUId;
        }

        public void setOUId(String OUId) {
            this.OUId = OUId;
        }

        public String getOUName() {
            return OUName;
        }

        public void setOUName(String OUName) {
            this.OUName = OUName;
        }

        public String getRankId() {
            return RankId;
        }

        public void setRankId(String RankId) {
            this.RankId = RankId;
        }

        public String getRankName() {
            return RankName;
        }

        public void setRankName(String RankName) {
            this.RankName = RankName;
        }

        public Object getPositionDuty() {
            return PositionDuty;
        }

        public void setPositionDuty(Object PositionDuty) {
            this.PositionDuty = PositionDuty;
        }

        public Object getCurrentAccountSetName() {
            return CurrentAccountSetName;
        }

        public void setCurrentAccountSetName(Object CurrentAccountSetName) {
            this.CurrentAccountSetName = CurrentAccountSetName;
        }

        public List<UserPositionsEntity> getUserPositions() {
            return UserPositions;
        }

        public void setUserPositions(List<UserPositionsEntity> UserPositions) {
            this.UserPositions = UserPositions;
        }

        public List<?> getAccountSets() {
            return AccountSets;
        }

        public void setAccountSets(List<?> AccountSets) {
            this.AccountSets = AccountSets;
        }

        public static class UserPositionsEntity {
            private String PositionId;
            private String PositionName;
            private Object PositionDuty;

            public String getPositionId() {
                return PositionId;
            }

            public void setPositionId(String PositionId) {
                this.PositionId = PositionId;
            }

            public String getPositionName() {
                return PositionName;
            }

            public void setPositionName(String PositionName) {
                this.PositionName = PositionName;
            }

            public Object getPositionDuty() {
                return PositionDuty;
            }

            public void setPositionDuty(Object PositionDuty) {
                this.PositionDuty = PositionDuty;
            }
        }
    }
}

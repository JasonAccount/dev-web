package plugins.page;

public class Test {

    /*public static void main(String[] args) throws IOException {
        String resoutce = "mybatis-config.xml";

        InputStream resourceAsStream = Resources.getResourceAsStream(resoutce);

        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = sessionFactory.openSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Map<String, Object> params = new HashMap<>();
        params.put("nickName", "Jason");
        Page page = new Page.PageBuilder()
                .pageNo(1)
                .pageSize(1)
                .isTotal(true)
                .params(params)
                .build();
        List<User> users = mapper.findByPage(page);

        for (User user : users) {
            System.out.println(user.getName());
        }
    }*/
}

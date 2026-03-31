public interface CatRepository extends JpaRepository<Cat, Long> {
    
//新增 修改 删除（必须加 @Modifying + @Transactional）
//Transactional应该加到Service方法上,两个必须同时存在否则报错


reposity有写好的默认查询方式
find By/count By/exists By +属性名+操作+连接词

    // 精确匹配
    List<Cat> findByName(String name);
    // AND 条件
    List<Cat> findByNameAndAge(String name, int age);
    // OR 条件
    List<Cat> findByNameOrBreed(String name, String breed);

    // 模糊查询
    List<Cat> findByNameLike(String name);           // 需自己加 %
    List<Cat> findByNameContaining(String name);     // 自动加 %name%

    // 比较
    List<Cat> findByAgeGreaterThan(int age);
    List<Cat> findByAgeLessThanEqual(int age);
    List<Cat> findByAgeBetween(int min, int max);

    // 排序
    List<Cat> findByBreedOrderByAgeDesc(String breed);

    // 限制数量
    List<Cat> findTop3ByOrderByAgeDesc();

    // 统计
    long countByBreed(String breed);
    boolean existsByName(String name)



下面的  (:name)  表示的是命名参数

//也有JPQL

@Query("SELECT c FROM Cat c WHERE c.age > :minAge AND c.breed = :breed")
List<Cat> findAdultCatsByBreed(@Param("minAge") int minAge, 
                                @Param("breed") String breed);
@Param(与@Q的一致)是把传入变量和jpql的参数绑定


// 原生 SQL 
@Query(value = "SELECT * FROM cat WHERE name LIKE CONCAT('%', :keyword, '%')", 
       nativeQuery = true)
List<Cat> searchByNameNative(@Param("keyword") String keyword);
//                            ↑ 绑定到 :keyword

// 修改（必须加 @Modifying + @Transactional）
//Transactional应该加到Service方法上,两个必须同时存在否则报错
@Modifying
@Query("UPDATE Cat c SET c.age = c.age + 1 WHERE c.id = :id")
int increaseAge(@Param("id") Long id);

// 删除
@Modifying
@Query("DELETE FROM Cat c WHERE c.age > :age")
int deleteOldCats(@Param("age") int age);


//增加
使用reposity自带的save方法
使用@Querry+sql语句

}
package com.example.cud;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.example.cud.example.ExampleTable1;
import com.example.cud.example.ExampleTable2;
import com.example.cud.example.ExampleTable3;
import com.example.cud.example.ExampleTable4;
import com.example.cud.example.ExampleTable5;
import com.example.cud.impl.AutoCudServiceImpl;
import com.example.cud.impl.EntityMetaFactoryImpl;
import com.example.cud.impl.NamingConventionImpl;
import com.zaxxer.hikari.HikariDataSource;

public class AutoCudServiceTest {

	AutoCudService sut;
	JdbcTemplate jdbc;
	HikariDataSource hikariDataSource;

	@BeforeEach
	void init() {
		hikariDataSource = new HikariDataSource();
		hikariDataSource
				.setJdbcUrl("jdbc:tc:postgresql:14.2:///example?TC_DAEMON=true&TC_INITSCRIPT=init-for-autocud.sql");
		TransactionAwareDataSourceProxy dataSource = new TransactionAwareDataSourceProxy(hikariDataSource);
		jdbc = new JdbcTemplate(dataSource);

		NamingConvention namingConvention = new NamingConventionImpl();
		EntityMetaFactory entityMetaFactory = new EntityMetaFactoryImpl(dataSource, namingConvention);
		sut = new AutoCudServiceImpl(jdbc, entityMetaFactory);
	}

	@AfterEach
	void close() {
		hikariDataSource.close();
	}

	@Test
	void insert() {
		ExampleTable1 entity = new ExampleTable1();
		entity.setExampleCol1("foo");
		entity.setExampleCol2("bar");

		int insertedCount = sut.insert(entity);

		assertEquals(1, insertedCount);
		assertNotNull(entity.getId());
		assertEquals(1, entity.getVersion());

		ExampleTable1 selected = jdbc.queryForObject("select * from example_table1 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable1.class), entity.getId());

		assertEquals(entity, selected);
	}

	@Test
	void insertNull() {
		ExampleTable1 entity = new ExampleTable1();
		entity.setExampleCol1("foobar");

		int insertedCount = sut.insert(entity);

		assertEquals(1, insertedCount);
		assertNotNull(entity.getId());
		assertEquals(1, entity.getVersion());

		ExampleTable1 selected = jdbc.queryForObject("select * from example_table1 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable1.class), entity.getId());

		assertEquals(entity, selected);
	}

	@Test
	void insertExcludesNull() {
		ExampleTable1 entity = new ExampleTable1();

		int insertedCount = sut.insertExcludesNull(entity);

		assertEquals(1, insertedCount);
		assertNotNull(entity.getId());
		assertEquals(1, entity.getVersion());

		ExampleTable1 selected = jdbc.queryForObject("select * from example_table1 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable1.class), entity.getId());

		entity.setExampleCol1("a");
		entity.setExampleCol2("b");
		assertEquals(entity, selected);
	}

	@Test
	void update() {
		jdbc.execute("insert into example_table1 (version) values (1)");
		int id = jdbc.queryForObject("select currval(pg_get_serial_sequence('example_table1', 'id'))", int.class);

		ExampleTable1 entity = new ExampleTable1();
		entity.setId(id);
		entity.setExampleCol1("foo");
		entity.setExampleCol2("bar");
		entity.setVersion(1);

		int updatedCount = sut.update(entity);

		assertEquals(1, updatedCount);
		assertEquals(2, entity.getVersion());

		ExampleTable1 selected = jdbc.queryForObject("select * from example_table1 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable1.class), entity.getId());

		assertEquals(entity, selected);
	}

	@Test
	void updateOptimisticLockException() {
		jdbc.execute("insert into example_table1 (version) values (1)");
		int id = jdbc.queryForObject("select currval(pg_get_serial_sequence('example_table1', 'id'))", int.class);

		ExampleTable1 entity = new ExampleTable1();
		entity.setId(id);
		entity.setExampleCol1("foo");
		entity.setExampleCol2("bar");
		entity.setVersion(0);

		assertThrows(OptimisticLockException.class, () -> sut.update(entity));
	}

	@Test
	void updateNull() {
		jdbc.execute("insert into example_table1 (version) values (1)");
		int id = jdbc.queryForObject("select currval(pg_get_serial_sequence('example_table1', 'id'))", int.class);

		ExampleTable1 entity = new ExampleTable1();
		entity.setId(id);
		entity.setExampleCol1("foobar");
		entity.setVersion(1);

		int updatedCount = sut.update(entity);

		assertEquals(1, updatedCount);
		assertEquals(2, entity.getVersion());

		ExampleTable1 selected = jdbc.queryForObject("select * from example_table1 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable1.class), entity.getId());

		assertEquals(entity, selected);
	}

	@Test
	void updateExcludesNull() throws Exception {
		jdbc.execute("insert into example_table1 (version) values (1)");
		int id = jdbc.queryForObject("select currval(pg_get_serial_sequence('example_table1', 'id'))", int.class);

		ExampleTable1 entity = new ExampleTable1();
		entity.setId(id);
		entity.setExampleCol1("foobar");
		entity.setVersion(1);

		int updatedCount = sut.updateExcludesNull(entity);

		assertEquals(1, updatedCount);
		assertEquals(2, entity.getVersion());

		ExampleTable1 selected = jdbc.queryForObject("select * from example_table1 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable1.class), entity.getId());

		entity.setExampleCol2("b");
		assertEquals(entity, selected);
	}

	@Test
	void forceVersioningUpdate() {
		jdbc.execute("insert into example_table1 (version) values (1)");
		int id = jdbc.queryForObject("select currval(pg_get_serial_sequence('example_table1', 'id'))", int.class);

		ExampleTable1 entity = new ExampleTable1();
		entity.setId(id);
		entity.setExampleCol1("foo");
		entity.setExampleCol2("bar");

		int updatedCount = sut.forceVersioningUpdate(entity);

		assertEquals(1, updatedCount);

		ExampleTable1 selected = jdbc.queryForObject("select * from example_table1 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable1.class), entity.getId());

		entity.setVersion(2);
		assertEquals(entity, selected);
	}

	@Test
	void forceVersioningUpdateExcludesNull() {
		jdbc.execute("insert into example_table1 (version) values (1)");
		int id = jdbc.queryForObject("select currval(pg_get_serial_sequence('example_table1', 'id'))", int.class);

		ExampleTable1 entity = new ExampleTable1();
		entity.setId(id);
		entity.setExampleCol1("foobar");

		int updatedCount = sut.forceVersioningUpdateExcludesNull(entity);

		assertEquals(1, updatedCount);

		ExampleTable1 selected = jdbc.queryForObject("select * from example_table1 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable1.class), entity.getId());

		entity.setExampleCol2("b");
		entity.setVersion(2);
		assertEquals(entity, selected);
	}

	@Test
	void delete() {
		jdbc.execute("insert into example_table1 (version) values (1)");
		int id = jdbc.queryForObject("select currval(pg_get_serial_sequence('example_table1', 'id'))", int.class);

		ExampleTable1 entity = new ExampleTable1();
		entity.setId(id);

		int updatedCount = sut.delete(entity);

		assertEquals(1, updatedCount);

		int count = jdbc.queryForObject("select count(*) from example_table1 where id = ?", int.class, entity.getId());

		assertEquals(0, count);
	}

	@Test
	void insertNotAutoIncrement() {
		ExampleTable2 entity = new ExampleTable2();
		entity.setId(123);
		entity.setExampleCol1("foo");
		entity.setExampleCol2("bar");

		int insertedCount = sut.insert(entity);

		assertEquals(1, insertedCount);
		assertEquals(123, entity.getId());
		assertEquals(1, entity.getVersion());

		ExampleTable2 selected = jdbc.queryForObject("select * from example_table2 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable2.class), entity.getId());

		assertEquals(entity, selected);
	}

	@Test
	void insertCompositePrimaryKeys() {
		ExampleTable3 entity = new ExampleTable3();
		entity.setId1(123);
		entity.setId2("xyz");
		entity.setExampleCol1("foo");
		entity.setExampleCol2("bar");

		int insertedCount = sut.insert(entity);

		assertEquals(1, insertedCount);
		assertEquals(123, entity.getId1());
		assertEquals("xyz", entity.getId2());
		assertEquals(1, entity.getVersion());

		ExampleTable3 selected = jdbc.queryForObject("select * from example_table3 where id1 = ? and id2 = ?",
				new BeanPropertyRowMapper<>(ExampleTable3.class), entity.getId1(), entity.getId2());

		assertEquals(entity, selected);
	}

	@Test
	void insertNoPrimaryKey() {
		ExampleTable5 entity = new ExampleTable5();
		entity.setExampleCol1("foo");
		entity.setExampleCol2("bar");

		int insertedCount = sut.insert(entity);

		assertEquals(1, insertedCount);

		ExampleTable5 selected = jdbc.queryForObject("select * from example_table5 limit 1",
				new BeanPropertyRowMapper<>(ExampleTable5.class));

		assertEquals(entity, selected);
	}

	@Test
	void updateNoVersion() {
		jdbc.execute("insert into example_table4 (example_col1, example_col2) values ('foo', 'bar')");
		int id = jdbc.queryForObject("select currval(pg_get_serial_sequence('example_table4', 'id'))", int.class);

		ExampleTable4 entity = new ExampleTable4();
		entity.setId(id);
		entity.setExampleCol1("baz");
		entity.setExampleCol2("qux");

		int updatedCount = sut.update(entity);

		assertEquals(1, updatedCount);

		ExampleTable4 selected = jdbc.queryForObject("select * from example_table4 where id = ?",
				new BeanPropertyRowMapper<>(ExampleTable4.class), entity.getId());

		assertEquals(entity, selected);
	}

	@Test
	void canNotUpdateNoPrimaryKey() {
		ExampleTable5 entity = new ExampleTable5();
		entity.setExampleCol1("baz");
		entity.setExampleCol2("qux");

		assertThrows(AutoCudException.class, () -> sut.update(entity));
	}

	@Test
	void canNotDeleteNoPrimaryKey() {
		ExampleTable5 entity = new ExampleTable5();

		assertThrows(AutoCudException.class, () -> sut.delete(entity));
	}
}
